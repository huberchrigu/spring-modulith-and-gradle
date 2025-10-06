package tech.chrigu.spring.modulith.portfolio.company.mongo

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.portfolio.company.Company
import tech.chrigu.spring.modulith.portfolio.company.CompanyId

interface CompanyRepository : CoroutineCrudRepository<Company, CompanyId> {
    fun findByName(name: String): Flow<Company>
}
