package tech.chrigu.spring.modulith.portfolio.company

import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.*

data class PortfolioCompany(val id: PortfolioCompanyId, val name: String, val offeredServices: List<ServiceId>) {
    fun add(serviceId: ServiceId): PortfolioCompany {
        require(offeredServices.contains(serviceId).not())
        return copy(offeredServices = offeredServices + serviceId)
    }
}

class PortfolioCompanyId(uuid: UUID) : AbstractIdentifier(uuid) {
    companion object : IdentifierObject<PortfolioCompanyId>({ PortfolioCompanyId(it) })
}