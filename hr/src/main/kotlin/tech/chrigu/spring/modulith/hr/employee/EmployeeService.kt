package tech.chrigu.spring.modulith.hr.employee

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.employee.mongo.EmployeeRepository

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {
    suspend fun getOrCreate(name: String): Employee {
        return employeeRepository.findByName(name) ?: employeeRepository.save(Employee(null, name, emptyList()))
    }
}
