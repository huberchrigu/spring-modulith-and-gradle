package tech.chrigu.spring.modulith.hr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.modulith.Modulithic

@SpringBootApplication
@Import(HrTestcontainersConfiguration::class, HrTestDataLoader::class)
class TestHrModule

fun main(args: Array<String>) {
    runApplication<TestHrModule>(*args)
}
