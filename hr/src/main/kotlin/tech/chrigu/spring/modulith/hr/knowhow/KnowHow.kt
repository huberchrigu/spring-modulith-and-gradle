package tech.chrigu.spring.modulith.hr.knowhow

import org.jmolecules.ddd.types.AggregateRoot
import tech.chrigu.spring.modulith.hr.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.hr.shared.IdentifierObject
import java.util.UUID

data class KnowHow(override val id: KnowHowId, val title: String) : AggregateRoot<KnowHow, KnowHowId>

class KnowHowId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<KnowHowId>(::KnowHowId)
}
