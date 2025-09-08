package tech.chrigu.spring.modulith.portfolio.company

import tech.chrigu.spring.modulith.portfolio.service.ServiceId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.*

internal data class Company(val id: CompanyId, val name: String, val offeredServices: List<ServiceId>)

internal class CompanyId(uuid: UUID) : AbstractIdentifier(uuid) {
    companion object : IdentifierObject<CompanyId>({ CompanyId(it) })
}