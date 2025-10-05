package tech.chrigu.spring.modulith.portfolio

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(properties = ["logging.level.org.springframework.boot.autoconfigure=DEBUG"])
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class PortfolioModuleTest(private val webTestClient: WebTestClient, private val testData: PortfolioTestDataLoader) {
    @Test
    fun `should create skill`() {
        webTestClient.post().uri("/skills")
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
    }

    @Test
    fun `should create service`() {
        webTestClient.post().uri("/services")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"title":  "Kotlin Coding", "requiredSkills":  ["${testData.kotlinId}"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("name").isEqualTo("Kotlin Coding")
            .jsonPath("requiredSkills").isEqualTo(testData.kotlinId)
            .jsonPath("id").isNotEmpty
    }

    @Test
    fun `should add required skill`() {
        webTestClient.post().uri("/services/{id}/required-skills", testData.serviceId)
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
            .jsonPath("name").isEqualTo(testData.serviceTitle)
            .jsonPath("requiredSkills").value<List<Any>> { assertThat(it).containsExactly(testData.cssId, testData.kotlinId) }
    }

    @Test
    fun `should remove skill`() {
        webTestClient.delete().uri("/services/{id}/required-skills/{skill}", testData.serviceId, testData.cssId)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.serviceId)
            .jsonPath("name").isEqualTo(testData.serviceTitle)
            .jsonPath("requiredSkills").isEmpty
    }

    @Test
    fun `should find company by name`() {
        webTestClient.get().uri("/companies?name={name}", testData.companyName)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.companyId)
            .jsonPath("name").isEqualTo(testData.companyName)
            .jsonPath("offeredServices").isEqualTo(testData.serviceId)
    }

    @Test
    fun `should add service to company`() {
        webTestClient.post().uri("/companies/{id}/services", testData.companyId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"service":  "${testData.serviceTitle2}"}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("id").isEqualTo(testData.companyId)
            .jsonPath("name").isEqualTo(testData.companyName)
            .jsonPath("services").value<List<Any>> { assertThat(it).containsExactly(testData.serviceId, testData.serviceId2) }
    }

    @Test
    fun `should create company`() {
        webTestClient.post().uri("/companies")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {"name":  "Per Müller AG", "offeredServices":  ["${testData.serviceId2}"]}
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("id").isNotEmpty
            .jsonPath("name").isEqualTo("Per Müller AG")
            .jsonPath("offeredServices").value<List<String>> { assertThat(it).containsExactly(testData.serviceId2) }
    }
}
