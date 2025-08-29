package tech.chrigu.spring.modulith.hr.shared

import org.jmolecules.ddd.types.Identifier
import java.io.Serializable
import java.util.UUID

abstract class AbstractIdentifier(val id: UUID) : Identifier, Serializable {
    override fun toString() = id.toString()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractIdentifier) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

open class IdentifierObject<T : Identifier>(private val create: (UUID) -> T) {
    fun newId() = create(UUID.randomUUID())
}
