package tech.chrigu.spring.modulith.portfolio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(PortfolioTestcontainersConfiguration::class, PortfolioTestDataLoader::class)
class TestPortfolioModule

fun main(args: Array<String>) {
    runApplication<TestPortfolioModule>(*args)
}
