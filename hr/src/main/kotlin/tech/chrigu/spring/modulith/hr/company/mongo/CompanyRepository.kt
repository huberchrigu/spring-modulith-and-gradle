package tech.chrigu.spring.modulith.hr.company.mongo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.hr.company.Company
import tech.chrigu.spring.modulith.hr.company.CompanyId

interface CompanyRepository : CoroutineCrudRepository<Company, CompanyId> {
    suspend fun findByName(name: String): Company
}
