package tech.chrigu.spring.modulith.portfolio.service.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.chrigu.spring.modulith.portfolio.service.Service
import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.portfolio.service.ServiceService
import tech.chrigu.spring.modulith.portfolio.shared.web.PortfolioApi
import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import java.net.URI

@RestController
@RequestMapping("${PortfolioApi.BASE_URI}/services")
@Tag(name = "Services", description = "Service Endpoints")
internal class ServiceRestController(private val serviceService: ServiceService) {
    @Operation(
        summary = "Create new service",
        description = "This creates a new service in the portfolio.\nNo authentication needed (yet)."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Service created",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class),
                        examples = [ExampleObject(
                            value = """{"id":"2b7b3358-81c1-448c-9075-18e698834138","title":"Kotlin Coding","description":"Programming in Kotlin","requiredSkills":["d141a2a2-3702-43df-8218-470cd4f698e8"]}"""
                        )]
                    )
                ]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Unexpected server error", content = [Content()])
        ]
    )
    @PostMapping
    suspend fun createService(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Service to create",
            required = true,
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = CreateServiceBody::class),
                examples = [ExampleObject(
                    value = """{"title":  "Kotlin Coding", "description": "Programming in Kotlin", "requiredSkills":  ["d141a2a2-3702-43df-8218-470cd4f698e8"]}}"""
                )]
            )]
        )
        @RequestBody body: CreateServiceBody
    ) = serviceService.create(body.title, body.description, body.requiredSkills)
        .let { ResponseEntity.created(URI("/portfolio/services/${it.id}")).body(it) }

    @PostMapping("/{id}/required-skills")
    suspend fun addSkill(@PathVariable id: ServiceId, @RequestBody body: AddSkillBody) = serviceService.addSkill(id, body.requiredSkill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}/required-skills/{skill}")
    suspend fun removeSkill(@PathVariable id: ServiceId, @PathVariable skill: SkillId) = serviceService.removeSkill(id, skill)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    data class CreateServiceBody(@RequestBody val title: String, val description: String, val requiredSkills: List<SkillId>)

    data class AddSkillBody(val requiredSkill: SkillId)
}
