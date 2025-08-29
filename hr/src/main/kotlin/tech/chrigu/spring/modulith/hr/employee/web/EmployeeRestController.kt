package tech.chrigu.spring.modulith.hr.employee.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.hr.employee.EmployeeService
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import java.net.URI

@RestController
@RequestMapping("/employees")
class EmployeeRestController(private val employeeService: EmployeeService) {
    @PostMapping
    suspend fun createEmployee(@RequestBody body: CreateEmployeeBody) = employeeService.create(body.name, body.skills)
        .let { ResponseEntity.created(URI.create("/employees/${it.id}")).body(it) }

    @PostMapping("/{id}/skills")
    suspend fun addSkill(@PathVariable id: EmployeeId, @RequestBody body: AddSkillBody) = employeeService.addSkill(id, body.skill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}/skills/{skill}")
    suspend fun removeSkill(@PathVariable id: EmployeeId, @PathVariable skill: KnowHowId) = employeeService.removeSkill(id, skill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    data class CreateEmployeeBody(val name: String, val skills: List<KnowHowId> = emptyList())
    data class AddSkillBody(val skill: KnowHowId)
}
