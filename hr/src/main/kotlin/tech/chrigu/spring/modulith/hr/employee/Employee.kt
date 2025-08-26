package tech.chrigu.spring.modulith.hr.employee

import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId

class Employee(private val id: EmployeeId?, private val name: String, private val skills: List<KnowHowId>)

@JvmInline
value class EmployeeId(private val id: String)
