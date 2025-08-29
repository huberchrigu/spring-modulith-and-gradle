package tech.chrigu.spring.modulith.hr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(HrTestcontainersConfiguration::class, HrTestDataLoader::class)
class TestHrModule

fun main(args: Array<String>) {
    runApplication<TestHrModule>(*args)
}
