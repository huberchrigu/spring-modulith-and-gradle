package tech.chrigu.spring.modulith.portfolio

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyId
import tech.chrigu.spring.modulith.hr.company.CompanyUpdatedEvent
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.company.PortfolioCompanyService
import tech.chrigu.spring.modulith.portfolio.skill.Skill
import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import tech.chrigu.spring.modulith.portfolio.skill.SkillService

@SpringBootTest(properties = ["logging.level.org.springframework.boot.autoconfigure=DEBUG"])
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class PortfolioModuleTest(
    private val webTestClient: WebTestClient, private val testData: PortfolioTestDataLoader,
    private val eventPublisher: ApplicationEventPublisher, private val companyService: PortfolioCompanyService,
    private val skillService: SkillService
) {
    @BeforeEach
    fun resetTestData() = runTest {
        testData.clear()
        testData.load()
    }

    @Test
    fun `should create skill`() = runTest {
        val knowHow = KnowHow(KnowHowId.newId(), "Java")
        eventPublisher.publishEvent(KnowHowUpdatedEvent(knowHow))
        Awaitility.await().until { runBlocking { skillService.findByName(knowHow.title) != null } }
        val skill = skillService.findByName(knowHow.title)
        val expected = Skill(SkillId(knowHow.id.id), knowHow.title)
        assertThat(skill).isEqualTo(expected)
    }

    @Test
    fun `should create service`() {
        webTestClient.post().uri("/portfolio/services")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"title":  "Kotlin Coding", "description": "Programming in Kotlin", "requiredSkills":  ["${testData.kotlinId}"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("title").isEqualTo("Kotlin Coding")
            .jsonPath("requiredSkills").isEqualTo(testData.kotlinId)
            .jsonPath("id").isNotEmpty
    }

    @Test
    fun `should add required skill`() {
        webTestClient.post().uri("/portfolio/services/{id}/required-skills", testData.serviceId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"requiredSkill":  "${testData.kotlinId}"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.serviceId)
            .jsonPath("title").isEqualTo(testData.serviceTitle)
            .jsonPath("requiredSkills").value<List<Any>> { assertThat(it).containsExactly(testData.cssId, testData.kotlinId) }
    }

    @Test
    fun `should remove skill`() {
        webTestClient.delete().uri("/portfolio/services/{id}/required-skills/{skill}", testData.serviceId, testData.cssId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.serviceId)
            .jsonPath("title").isEqualTo(testData.serviceTitle)
            .jsonPath("requiredSkills").isEmpty
    }

    @Test
    fun `should find company by name`() {
        webTestClient.get().uri("/portfolio/companies?name={name}", testData.companyName)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(testData.companyId)
            .jsonPath("$[0].name").isEqualTo(testData.companyName)
            .jsonPath("$[0].offeredServices").isEqualTo(testData.serviceId)
    }

    @Test
    fun `should add service to company`() {
        webTestClient.post().uri("/portfolio/companies/{id}/services", testData.companyId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"service":  "${testData.serviceId2}"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.companyId)
            .jsonPath("name").isEqualTo(testData.companyName)
            .jsonPath("offeredServices").value<List<Any>> { assertThat(it).containsExactly(testData.serviceId, testData.serviceId2) }
    }

    @Test
    fun `should create company`() = runTest {
        val company = Company(CompanyId.newId(), "Petr Müller AG", emptyList())
        eventPublisher.publishEvent(CompanyUpdatedEvent(company))
        val companies = companyService.findByName(company.name).toList()
        val expected = tech.chrigu.spring.modulith.portfolio.company.PortfolioCompany(
            tech.chrigu.spring.modulith.portfolio.company.PortfolioCompanyId(company.id.id),
            company.name,
            emptyList()
        )
        assertThat(companies).containsExactly(expected)
    }
}
