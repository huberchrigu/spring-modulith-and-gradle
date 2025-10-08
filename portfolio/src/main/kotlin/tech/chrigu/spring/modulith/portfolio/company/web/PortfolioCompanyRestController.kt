package tech.chrigu.spring.modulith.portfolio.company.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.chrigu.spring.modulith.portfolio.company.PortfolioCompanyId
import tech.chrigu.spring.modulith.portfolio.company.PortfolioCompanyService
import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.portfolio.shared.web.PortfolioApi

@RestController
@RequestMapping("${PortfolioApi.BASE_URI}/companies")
class PortfolioCompanyRestController(private val companyService: PortfolioCompanyService) {
    @GetMapping
    fun findCompanies(@RequestParam name: String) = companyService.findByName(name)

    @PostMapping("/{id}/services")
    suspend fun addService(@PathVariable id: PortfolioCompanyId, @RequestBody body: AddServiceBody) = companyService.addService(id, body.service)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    data class AddServiceBody(val service: ServiceId)
}
