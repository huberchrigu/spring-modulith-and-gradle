package tech.chrigu.spring.modulith.hr

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(HrTestcontainersConfiguration::class)
@SpringBootTest
class TestHrModule {
    @Test
    fun contextLoads() {
    }
}

