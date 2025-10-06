package tech.chrigu.spring.modulith.portfolio.company

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.modulith.ApplicationModuleListener
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.company.CompanyDeletedEvent
import tech.chrigu.spring.modulith.hr.company.CompanyUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.company.mongo.CompanyRepository
import tech.chrigu.spring.modulith.portfolio.service.ServiceId

@Service
class CompanyService(private val companyRepository: CompanyRepository, private val coroutineScope: CoroutineScope) {
    suspend fun create(name: String, services: List<ServiceId>) = companyRepository.save(Company(CompanyId.newId(), name, services))
    fun findByName(name: String) = companyRepository.findByName(name)

    suspend fun addService(id: CompanyId, service: ServiceId) = companyRepository.findById(id)
        ?.add(service)

    @ApplicationModuleListener
    fun on(event: CompanyUpdatedEvent) = coroutineScope.launch {
        companyRepository.save(event.company.toDomain())
    }

    @ApplicationModuleListener
    fun on(event: CompanyDeletedEvent) = coroutineScope.launch {
        companyRepository.delete(event.company.toDomain())
    }

    private fun tech.chrigu.spring.modulith.hr.company.Company.toDomain() = Company(id.toDomain(), name, emptyList())
    private fun tech.chrigu.spring.modulith.hr.company.CompanyId.toDomain() = CompanyId(id)
}
