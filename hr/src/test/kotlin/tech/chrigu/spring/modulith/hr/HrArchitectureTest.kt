package tech.chrigu.spring.modulith.hr

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class HrArchitectureTest {
    private val modules = ApplicationModules.of(TestHrModule::class.java)

    @Test
    fun writeDocumentationSnippets() {
        Documenter(modules).writeDocumentation()
    }
}
