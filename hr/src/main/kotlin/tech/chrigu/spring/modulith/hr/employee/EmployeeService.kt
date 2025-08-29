package tech.chrigu.spring.modulith.hr.employee

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.employee.mongo.EmployeeRepository
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService

@Service
class EmployeeService(private val employeeRepository: EmployeeRepository, private val knowHowService: KnowHowService) {
    suspend fun getOrCreate(name: String): Employee {
        return employeeRepository.findByName(name) ?: employeeRepository.save(Employee(EmployeeId.newId(), name, emptyList()))
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
}
