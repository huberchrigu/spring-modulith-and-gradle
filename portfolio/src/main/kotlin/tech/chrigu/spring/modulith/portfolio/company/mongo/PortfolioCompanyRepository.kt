package tech.chrigu.spring.modulith.portfolio.company.mongo

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.portfolio.company.PortfolioCompany
import tech.chrigu.spring.modulith.portfolio.company.PortfolioCompanyId

interface PortfolioCompanyRepository : CoroutineCrudRepository<PortfolioCompany, PortfolioCompanyId> {
    fun findByName(name: String): Flow<PortfolioCompany>
}
