package tech.chrigu.spring.modulith.hr.company.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.chrigu.spring.modulith.hr.company.CompanyService

@RestController
@RequestMapping("/companies")
class CompanyRestController(private val companyService: CompanyService) {
    @GetMapping
    suspend fun findByName(name: String) = companyService.findByName(name)
}
