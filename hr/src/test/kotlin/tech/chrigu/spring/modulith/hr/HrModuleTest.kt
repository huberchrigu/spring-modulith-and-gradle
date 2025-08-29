package tech.chrigu.spring.modulith.hr

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(properties = ["logging.level.org.springframework.boot.autoconfigure=DEBUG"])
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HrModuleTest(private val webTestClient: WebTestClient, private val testData: HrTestDataLoader) {
    @BeforeEach
    fun init() {
        runBlocking {
            testData.clear()
            testData.load()
        }
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
    }
}
