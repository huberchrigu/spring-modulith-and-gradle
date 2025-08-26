package tech.chrigu.spring.modulith.hr.company

import tech.chrigu.spring.modulith.hr.employee.EmployeeId

data class Company(private val id: CompanyId, private val name: String, private val employees: List<EmployeeId>)

@JvmInline
value class CompanyId(private val id: String)
