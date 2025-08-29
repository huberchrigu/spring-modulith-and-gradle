package tech.chrigu.spring.modulith.hr.employee.mongo

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.hr.employee.Employee
import tech.chrigu.spring.modulith.hr.employee.EmployeeId

interface EmployeeRepository : CoroutineCrudRepository<Employee, EmployeeId> {
    suspend fun findByName(name: String): Flow<Employee>
}
