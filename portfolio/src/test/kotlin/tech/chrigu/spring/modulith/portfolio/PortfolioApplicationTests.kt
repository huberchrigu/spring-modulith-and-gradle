package tech.chrigu.spring.modulith.portfolio

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(PortfolioTestcontainersConfiguration::class)
@SpringBootTest
class PortfolioApplicationTests {
    @Test
    fun contextLoads() {
    }
}
