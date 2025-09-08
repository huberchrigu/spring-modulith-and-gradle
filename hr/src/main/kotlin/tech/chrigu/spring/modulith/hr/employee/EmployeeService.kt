package tech.chrigu.spring.modulith.hr.employee

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.employee.mongo.EmployeeRepository
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService

@Service
internal class EmployeeService(private val employeeRepository: EmployeeRepository, private val knowHowService: KnowHowService) {
    suspend fun getOrCreate(name: String): Employee {
        val one = employeeRepository.findByName(name).toList()
            .let {
                if (it.isEmpty()) {
                    null
                } else {
                    check(it.size == 1) { "More than one employee found for name '$name'" }
                    it.first()
                }
            }
        return one ?: employeeRepository.save(Employee(EmployeeId.newId(), name, emptyList()))
    }

    suspend fun addSkill(id: EmployeeId, skill: KnowHowId): Employee? {
        val employee = employeeRepository.findById(id) ?: return null
        require(knowHowService.exists(skill))
        return employee.addSkill(skill)
    }

    suspend fun removeSkill(id: EmployeeId, skill: KnowHowId): Employee? {
        val employee = employeeRepository.findById(id) ?: return null
        require(knowHowService.exists(skill))
        return employee.removeSkill(skill)
    }

    suspend fun create(name: String, skills: List<KnowHowId>) = employeeRepository.save(Employee(EmployeeId.newId(), name, skills))
    suspend fun clear() {
        employeeRepository.deleteAll()
    }

    suspend fun findByName(name: String): Flow<Employee> {
        return employeeRepository.findByName(name)
    }
}
