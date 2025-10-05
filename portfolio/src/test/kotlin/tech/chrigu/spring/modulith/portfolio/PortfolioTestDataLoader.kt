package tech.chrigu.spring.modulith.portfolio

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import tech.chrigu.spring.modulith.portfolio.company.Company
import tech.chrigu.spring.modulith.portfolio.company.CompanyService
import tech.chrigu.spring.modulith.portfolio.service.Service
import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.portfolio.service.ServiceService
import tech.chrigu.spring.modulith.portfolio.skill.Skill
import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import tech.chrigu.spring.modulith.portfolio.skill.SkillService

@Component
internal class PortfolioTestDataLoader(
    private val skillService: SkillService,
    private val serviceService: ServiceService,
    private val companyService: CompanyService
) {
    lateinit var css: Skill
    lateinit var kotlin: Skill
    lateinit var service: Service
    lateinit var service2: Service
    lateinit var company: Company

    val cssName = "CSS"
    val kotlinName = "Kotlin"
    val serviceTitle = "Web Architecture"
    val serviceDescription = "Frontend, backend and architecture skills"
    val serviceTitle2 = "Food Processing"
    val serviceDescription2 = "Specialized in milk and cheese"
    val companyName = "Milch & Käse"

    val cssId
        get() = css.id.toString()
    val kotlinId
        get() = kotlin.id.toString()

    val serviceId
        get() = service.id.toString()
    val serviceId2
        get() = service2.id.toString()

    val companyId
        get() = company.id.toString()

    @PostConstruct
    fun load() = runBlocking {
        css = skill(cssName)
        kotlin = skill(kotlinName)
        service = service(serviceTitle, serviceDescription, listOf(css.id))
        service2 = service(serviceTitle2, serviceDescription2, emptyList())
        company = company(companyName, listOf(service.id))
    }

    private suspend fun skill(name: String) = skillService.findByName(name) ?: skillService.create(name)
    private suspend fun service(title: String, description: String, requiredSkills: List<SkillId>) =
        serviceService.findByTitle(title).firstOrNull() ?: serviceService.create(title, description, requiredSkills)

    private suspend fun company(name: String, services: List<ServiceId>) = companyService.findByName(name).firstOrNull() ?: companyService.create(name, services)
}
