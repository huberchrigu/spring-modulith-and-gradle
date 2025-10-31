package tech.chrigu.spring.modulith

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@Import(TestcontainersConfiguration::class, TestDataLoader::class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpringModulithAndGradleApplicationTests(private val webTestClient: WebTestClient, private val data: TestDataLoader) {
    @Test
    fun `should create service for hr company`() {
        webTestClient.post().uri("/portfolio/services")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                """
                {
                "title": "Kotlin Coding",
                "description": "Programming in Kotlin",
                "requiredSkills": ["${data.knowHow.id}"]
                }
            """.trimIndent()
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("id").isNotEmpty
            .jsonPath("requiredSkills").value<List<String>> { assertThat(it).containsExactly(data.knowHow.id.toString()) }
    }
}
