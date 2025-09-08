package tech.chrigu.spring.modulith.hr.company.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.chrigu.spring.modulith.hr.company.CompanyId
import tech.chrigu.spring.modulith.hr.company.CompanyService
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import java.net.URI

@RestController
@RequestMapping("/companies")
internal class CompanyRestController(private val companyService: CompanyService) {
    @GetMapping
    suspend fun findByName(name: String) = companyService.findByName(name)

    @PostMapping("/{id}/employees")
    suspend fun addEmployee(@PathVariable id: CompanyId, @RequestBody body: AddEmployeeBody) = companyService.addToCompany(id, body.name)
        ?.let { ResponseEntity.ok(it) }
        ?: ResponseEntity.notFound().build()

    @PostMapping
    suspend fun createCompany(@RequestBody body: CreateCompanyBody) = companyService.create(body.name, body.employees)
        .let { ResponseEntity.created(URI.create("/company/${it.id}")).body(it) }

    data class AddEmployeeBody(val name: String)

    data class CreateCompanyBody(val name: String, val employees: List<EmployeeId>)
}
