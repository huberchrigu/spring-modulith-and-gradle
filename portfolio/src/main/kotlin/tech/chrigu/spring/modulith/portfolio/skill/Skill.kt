package tech.chrigu.spring.modulith.portfolio.skill

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.UUID

@Document
internal data class Skill(val id: SkillId, @field:Indexed(unique = true) val name: String)

internal class SkillId(uuid: UUID) : AbstractIdentifier(uuid) {
    companion object : IdentifierObject<SkillId>({ SkillId(it) })
}