package tech.chrigu.spring.modulith.portfolio.company.mongo

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.CrudRepository
import tech.chrigu.spring.modulith.portfolio.company.Company
import tech.chrigu.spring.modulith.portfolio.company.CompanyId

internal interface CompanyRepository : CrudRepository<Company, CompanyId> {
    fun findByName(name: String): Flow<Company>
}
