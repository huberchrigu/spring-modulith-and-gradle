package tech.chrigu.spring.modulith.portfolio.service.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.portfolio.service.ServiceService
import tech.chrigu.spring.modulith.portfolio.shared.web.PortfolioApi
import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import java.net.URI

@RestController
@RequestMapping("${PortfolioApi.BASE_URI}/services")
internal class ServiceRestController(private val serviceService: ServiceService) {
    @PostMapping
    suspend fun createService(@RequestBody body: CreateServiceBody) = serviceService.create(body.title, body.description, body.requiredSkills)
        .let { ResponseEntity.created(URI("/portfolio/services/${it.id}")).body(it) }

    @PostMapping("/{id}/required-skills")
    suspend fun addSkill(@PathVariable id: ServiceId, @RequestBody body: AddSkillBody) = serviceService.addSkill(id, body.requiredSkill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}/required-skills/{skill}")
    suspend fun removeSkill(@PathVariable id: ServiceId, @PathVariable skill: SkillId) = serviceService.removeSkill(id, skill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    data class CreateServiceBody(val title: String, val description: String, val requiredSkills: List<SkillId>)

    data class AddSkillBody(val requiredSkill: SkillId)
}
