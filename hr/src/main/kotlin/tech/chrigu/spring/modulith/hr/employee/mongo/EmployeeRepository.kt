package tech.chrigu.spring.modulith.hr.employee.mongo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.hr.employee.Employee
import tech.chrigu.spring.modulith.hr.employee.EmployeeId

interface EmployeeRepository : CoroutineCrudRepository<Employee, EmployeeId> {
    suspend fun findByName(name: String): Employee?
}
