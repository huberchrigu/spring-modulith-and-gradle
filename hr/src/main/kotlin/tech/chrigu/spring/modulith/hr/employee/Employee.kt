package tech.chrigu.spring.modulith.hr.employee

import org.jmolecules.ddd.types.AggregateRoot
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.UUID

data class Employee(override val id: EmployeeId, val name: String, val skills: List<KnowHowId>) : AggregateRoot<Employee, EmployeeId> {
    fun addSkill(skill: KnowHowId): Employee {
        return copy(skills = skills + skill)
    }

    fun removeSkill(skill: KnowHowId): Employee {
        return copy(skills = skills - skill)
    }
}

class EmployeeId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<EmployeeId>(::EmployeeId)
}
