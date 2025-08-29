package tech.chrigu.spring.modulith.hr.knowhow

import org.jmolecules.ddd.types.AggregateRoot
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.UUID

@Document
data class KnowHow(override val id: KnowHowId, @field:Indexed(unique = true) val title: String) : AggregateRoot<KnowHow, KnowHowId>

class KnowHowId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<KnowHowId>(::KnowHowId)
}
