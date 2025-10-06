package tech.chrigu.spring.modulith.portfolio.service

import tech.chrigu.spring.modulith.portfolio.skill.SkillId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.*

data class Service(val id: ServiceId, val title: String, val description: String, val requiredSkills: List<SkillId>) {
    fun add(skillId: SkillId): Service {
        require(requiredSkills.contains(skillId).not())
        return copy(requiredSkills = requiredSkills + skillId)
    }

    fun remove(skillId: SkillId): Service {
        require(requiredSkills.contains(skillId))
        return copy(requiredSkills = requiredSkills - skillId)
    }
}

class ServiceId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<ServiceId>({ ServiceId(it) })
}
