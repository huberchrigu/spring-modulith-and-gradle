package tech.chrigu.spring.modulith.portfolio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
class ListenerConfiguration {
    @Bean
    fun coroutineScope() = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}