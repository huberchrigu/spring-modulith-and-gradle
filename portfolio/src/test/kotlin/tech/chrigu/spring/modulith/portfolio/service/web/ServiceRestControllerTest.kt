package tech.chrigu.spring.modulith.portfolio.service.web

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import tech.chrigu.spring.modulith.portfolio.PortfolioTestDataLoader
import tech.chrigu.spring.modulith.portfolio.service.Service
import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.portfolio.service.ServiceService
import tech.chrigu.spring.modulith.portfolio.skill.SkillId

@WebFluxTest(ServiceRestController::class)
internal class ServiceRestControllerTest(
    @param:Autowired private val webTestClient: WebTestClient
) {
    @MockitoBean
    private lateinit var serviceService: ServiceService

    @MockitoBean
    private lateinit var portfolioTestDataLoader: PortfolioTestDataLoader

    private val title = "Kotlin Coding"
    private val description = "Programming in Kotlin"
    private val skill = SkillId.newId()

    @Test
    fun `should create service`() = runTest {
        whenever(serviceService.create(title, description, listOf(skill))) doReturn Service(ServiceId.newId(), title, description, listOf(skill))
        webTestClient.post().uri("/portfolio/services")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"title":  "$title", "description": "$description", "requiredSkills":  ["$skill"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("title").isEqualTo(title)
            .jsonPath("requiredSkills").isEqualTo(skill.toString())
            .jsonPath("id").isNotEmpty
    }
}