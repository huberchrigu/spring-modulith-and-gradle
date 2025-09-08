package tech.chrigu.spring.modulith.portfolio

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class PortfolioArchitectureTest {
    private val modules = ApplicationModules.of(TestPortfolioModule::class.java)

    @Test
    fun writeDocumentationSnippets() {
        Documenter(modules).writeDocumentation()
    }
}
