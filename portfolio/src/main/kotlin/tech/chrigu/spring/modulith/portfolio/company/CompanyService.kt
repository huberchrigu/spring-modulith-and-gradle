package tech.chrigu.spring.modulith.portfolio.company

import kotlinx.coroutines.runBlocking
import org.springframework.modulith.ApplicationModuleListener
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.company.CompanyDeletedEvent
import tech.chrigu.spring.modulith.hr.company.CompanyUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.company.mongo.CompanyRepository
import tech.chrigu.spring.modulith.portfolio.service.ServiceId

@Service
internal class CompanyService(private val companyRepository: CompanyRepository) {
    fun create(name: String, services: List<ServiceId>) = companyRepository.save(Company(CompanyId.newId(), name, services))
    fun findByName(name: String) = companyRepository.findByName(name)

    @ApplicationModuleListener
    fun on(event: CompanyUpdatedEvent) = runBlocking {
        companyRepository.save(event.company.toDomain())
    }

    @ApplicationModuleListener
    fun on(event: CompanyDeletedEvent) = runBlocking {
        companyRepository.delete(event.company.toDomain())
    }

    private fun tech.chrigu.spring.modulith.hr.company.Company.toDomain() = Company(id.toDomain(), name, emptyList())
    private fun tech.chrigu.spring.modulith.hr.company.CompanyId.toDomain() = CompanyId(id)
}