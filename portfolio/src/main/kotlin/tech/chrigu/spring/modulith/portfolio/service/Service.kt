package tech.chrigu.spring.modulith.portfolio.service

import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.*

internal data class Service(val id: ServiceId, val title: String, val description: String, val requiredSkills: List<SkillId>)

internal class ServiceId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<ServiceId>({ ServiceId(it) })
}

