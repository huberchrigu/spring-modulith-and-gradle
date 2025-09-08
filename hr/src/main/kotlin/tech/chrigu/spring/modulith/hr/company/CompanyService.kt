package tech.chrigu.spring.modulith.hr.company

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.company.mongo.CompanyRepository
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.hr.employee.EmployeeService

@Service
internal class CompanyService(
    private val companyRepository: CompanyRepository,
    private val employeeService: EmployeeService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    suspend fun findByName(name: String) = companyRepository.findByName(name)
    suspend fun addToCompany(id: CompanyId, employeeName: String): Company? {
        val company = companyRepository.findById(id) ?: return null
        val employee = employeeService.getOrCreate(employeeName)
        return company.addEmployee(employee.id)
            .let { companyRepository.save(it) }
            .also { applicationEventPublisher.publishEvent(CompanyUpdatedEvent(it)) }
    }

    suspend fun create(name: String, employees: List<EmployeeId>) = companyRepository.save(Company(CompanyId.newId(), name, employees))
        .also { applicationEventPublisher.publishEvent(CompanyUpdatedEvent(it)) }

    suspend fun clear() {
        companyRepository.findAll()
            .collect { company ->
                companyRepository.delete(company)
                    .also { applicationEventPublisher.publishEvent(CompanyDeletedEvent(company)) }
            }
        companyRepository.deleteAll()
    }
}

data class CompanyUpdatedEvent(val company: Company) : ApplicationEvent(company)
data class CompanyDeletedEvent(val company: Company) : ApplicationEvent(company)