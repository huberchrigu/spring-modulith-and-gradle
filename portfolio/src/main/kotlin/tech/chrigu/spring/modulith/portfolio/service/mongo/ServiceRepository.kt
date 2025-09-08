package tech.chrigu.spring.modulith.portfolio.service.mongo

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.portfolio.service.Service
import tech.chrigu.spring.modulith.portfolio.service.ServiceId

internal interface ServiceRepository : CoroutineCrudRepository<Service, ServiceId> {
    fun findByTitle(title: String): Flow<Service>
}
