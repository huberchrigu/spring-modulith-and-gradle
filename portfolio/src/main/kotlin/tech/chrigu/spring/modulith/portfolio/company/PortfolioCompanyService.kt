package tech.chrigu.spring.modulith.portfolio.company

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyDeletedEvent
import tech.chrigu.spring.modulith.hr.company.CompanyId
import tech.chrigu.spring.modulith.hr.company.CompanyUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.company.mongo.PortfolioCompanyRepository
import tech.chrigu.spring.modulith.portfolio.service.ServiceId

@Service
class PortfolioCompanyService(private val companyRepository: PortfolioCompanyRepository, private val coroutineScope: CoroutineScope) {
    suspend fun create(name: String, services: List<ServiceId>) = companyRepository.save(PortfolioCompany(PortfolioCompanyId.newId(), name, services))
    fun findByName(name: String) = companyRepository.findByName(name)

    suspend fun addService(id: PortfolioCompanyId, service: ServiceId) = companyRepository.findById(id)
        ?.add(service)

    @EventListener
    fun on(event: CompanyUpdatedEvent) = coroutineScope.launch {
        companyRepository.save(event.company.toDomain())
    }

    @EventListener
    fun on(event: CompanyDeletedEvent) = coroutineScope.launch {
        companyRepository.delete(event.company.toDomain())
    }

    suspend fun clear() {
        companyRepository.deleteAll()
    }

    private fun Company.toDomain() = PortfolioCompany(id.toDomain(), name, emptyList())
    private fun CompanyId.toDomain() = PortfolioCompanyId(id)
}
