package tech.chrigu.spring.modulith

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

internal class ArchitectureTest {
    private val modules = ApplicationModules.of(SpringModulithAndGradleApplication::class.java)

    @Test
    fun writeDocumentationSnippets() {
        Documenter(modules).writeDocumentation()
    }
}
