package tech.chrigu.spring.modulith.hr

import org.springframework.stereotype.Component
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyService
import tech.chrigu.spring.modulith.hr.employee.Employee
import tech.chrigu.spring.modulith.hr.employee.EmployeeService
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService

@Component
class HrTestDataLoader(
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
        get() = css.id!!.toString()
    val kotlinId
        get() = kotlin.id!!.toString()

    val employeeId
        get() = employee.id!!.toString()
    val employeeId2
        get() = employee2.id!!.toString()

    val companyId
        get() = company.id!!.toString()

    suspend fun load() {
        css = knowHowService.create(cssName)
        kotlin = knowHowService.create(kotlinName)
        employee = employeeService.create(employeeName, listOf(css.id!!))
        employee2 = employeeService.create(employeeName2, emptyList())
        company = companyService.create(companyName, listOf(employee.id!!))
    }

    suspend fun clear() {
        knowHowService.clear()
        employeeService.clear()
        companyService.clear()
    }
}
