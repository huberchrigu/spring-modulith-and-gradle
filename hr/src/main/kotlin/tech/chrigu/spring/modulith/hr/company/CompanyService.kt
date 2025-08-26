package tech.chrigu.spring.modulith.hr.company

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.company.mongo.CompanyRepository
import tech.chrigu.spring.modulith.hr.employee.EmployeeService

@Service
class CompanyService(private val companyRepository: CompanyRepository, private val employeeService: EmployeeService) {
    suspend fun findByName(name: String) = companyRepository.findByName(name)
    suspend fun addToCompany(id: CompanyId, employeeName: String) = employeeService.getOrCreate(employeeName)
}

