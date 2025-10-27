package tech.chrigu.spring.modulith

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyService
import tech.chrigu.spring.modulith.hr.employee.Employee
import tech.chrigu.spring.modulith.hr.employee.EmployeeService
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService

@Component
class TestDataLoader(private val companyService: CompanyService, private val employeeService: EmployeeService,
                     private val knowHowService: KnowHowService
) {
    private lateinit var employee: Employee
    internal lateinit var company: Company
    lateinit var knowHow: KnowHow

    @PostConstruct
    fun load() = runBlocking {
        knowHow = knowHowService.create("Test Know-How")
        employee = employeeService.create("Test Employee", listOf(knowHow.id))
        company = companyService.create("Test Company", listOf(employee.id))
    }
}
