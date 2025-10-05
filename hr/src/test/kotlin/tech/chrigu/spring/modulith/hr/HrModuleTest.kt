package tech.chrigu.spring.modulith.hr

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyUpdatedEvent
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowUpdatedEvent

@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class HrModuleTest(
    private val webTestClient: WebTestClient,
    private val testData: HrTestDataLoader,
    private val objectMapper: ObjectMapper,
    private val eventListener: EventListener
) {
    @BeforeEach
    fun resetTestData() = runBlocking {
        testData.clear()
        testData.load()
        eventListener.events.clear()
    }

    @Test
    fun `should create know-how`() {
        webTestClient.post().uri("/know-how")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"title": "Java"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("title").isEqualTo("Java")
            .jsonPath("id").isNotEmpty
            .consumeWith {
                val id = objectMapper.readValue(it.responseBody, KnowHow::class.java).id
                assertThat(eventListener.events.filterIsInstance<KnowHowUpdatedEvent>()).containsExactly(
                    KnowHowUpdatedEvent(KnowHow(id, "Java"))
                )
            }
    }

    @Test
    fun `should create employee`() {
        webTestClient.post().uri("/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"name":  "Max Muster", "skills":  ["${testData.kotlinId}"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("name").isEqualTo("Max Muster")
            .jsonPath("skills").isEqualTo(testData.kotlinId)
            .jsonPath("id").isNotEmpty
    }

    @Test
    fun `should add skill`() {
        webTestClient.post().uri("/employees/{id}/skills", testData.employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"skill":  "${testData.kotlinId}"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.employeeId)
            .jsonPath("name").isEqualTo(testData.employeeName)
            .jsonPath("skills").value<List<Any>> { assertThat(it).containsExactly(testData.cssId, testData.kotlinId) }
    }

    @Test
    fun `should remove skill`() {
        webTestClient.delete().uri("/employees/{id}/skills/{skill}", testData.employeeId, testData.cssId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.employeeId)
            .jsonPath("name").isEqualTo(testData.employeeName)
            .jsonPath("skills").isEmpty
    }

    @Test
    fun `should find company by name`() {
        webTestClient.get().uri("/companies?name={name}", testData.companyName)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.companyId)
            .jsonPath("name").isEqualTo(testData.companyName)
            .jsonPath("employees").isEqualTo(testData.employeeId)
    }

    @Test
    fun `should add employee to company`() {
        webTestClient.post().uri("/companies/{id}/employees", testData.companyId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"name":  "${testData.employeeName2}"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.companyId)
            .jsonPath("name").isEqualTo(testData.companyName)
            .jsonPath("employees").value<List<Any>> { assertThat(it).containsExactly(testData.employeeId, testData.employeeId2) }
            .consumeWith {
                val id = objectMapper.readValue(it.responseBody, Company::class.java).id
                assertThat(eventListener.events.filterIsInstance<CompanyUpdatedEvent>())
                    .containsExactly(
                        CompanyUpdatedEvent(
                            Company(id, testData.companyName, listOf(testData.employeeId, testData.employeeId2).map(EmployeeId::of))
                        )
                    )
            }
    }

    @Test
    fun `should create company`() {
        webTestClient.post().uri("/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"name":  "Per Müller AG", "employees":  ["${testData.employeeId2}"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("id").isNotEmpty
            .jsonPath("name").isEqualTo("Per Müller AG")
            .jsonPath("employees").value<List<String>> { assertThat(it).containsExactly(testData.employeeId2) }
            .consumeWith {
                val id = objectMapper.readValue(it.responseBody, Company::class.java).id
                assertThat(eventListener.events.filterIsInstance<CompanyUpdatedEvent>()).containsExactly(
                    CompanyUpdatedEvent(
                        Company(id, "Per Müller AG", listOf(EmployeeId.of(testData.employeeId2)))
                    )
                )
            }
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun eventListener() = EventListener()
    }

    class EventListener : ApplicationListener<ApplicationEvent> {
        val events = mutableListOf<ApplicationEvent>()
        override fun onApplicationEvent(event: ApplicationEvent) {
            events.add(event)
        }
    }
}
