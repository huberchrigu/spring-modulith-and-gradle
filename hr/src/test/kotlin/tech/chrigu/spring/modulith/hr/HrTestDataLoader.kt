package tech.chrigu.spring.modulith.hr

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyService
import tech.chrigu.spring.modulith.hr.employee.Employee
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.hr.employee.EmployeeService
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService

@Component
internal class HrTestDataLoader(
    private val knowHowService: KnowHowService,
    private val employeeService: EmployeeService,
    private val companyService: CompanyService
) {
    lateinit var css: KnowHow
    lateinit var kotlin: KnowHow
    lateinit var employee: Employee
    lateinit var employee2: Employee
    lateinit var company: Company

    val cssName = "CSS"
    val kotlinName = "Kotlin"
    val employeeName = "Verena Müller"
    val employeeName2 = "Dietrich Werner"
    val companyName = "Milch & Käse"

    val cssId
        get() = css.id.toString()
    val kotlinId
        get() = kotlin.id.toString()

    val employeeId
        get() = employee.id.toString()
    val employeeId2
        get() = employee2.id.toString()

    val companyId
        get() = company.id.toString()

    @PostConstruct
    fun load() = runBlocking {
        css = knowHow(cssName)
        kotlin = knowHow(kotlinName)
        employee = employee(employeeName, listOf(css.id))
        employee2 = employee(employeeName2, emptyList())
        company = company(companyName, listOf(employee.id))
    }

    suspend fun clear() {
        knowHowService.clear()
        employeeService.clear()
        companyService.clear()
    }

    private suspend fun knowHow(name: String): KnowHow = knowHowService.findByTitle(name) ?: knowHowService.create(name)
    private suspend fun employee(name: String, skills: List<KnowHowId>) = employeeService.findByName(name).firstOrNull() ?: employeeService.create(name, skills)
    private suspend fun company(name: String, employees: List<EmployeeId>) = companyService.findByName(name) ?: companyService.create(name, employees)

}
