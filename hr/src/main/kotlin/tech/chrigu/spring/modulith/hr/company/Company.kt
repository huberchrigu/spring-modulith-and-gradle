package tech.chrigu.spring.modulith.hr.company

import org.jmolecules.ddd.types.AggregateRoot
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.shared.IdentifierObject
import java.util.UUID

@Document
data class Company(override val id: CompanyId, @field:Indexed(unique = true) val name: String, val employees: List<EmployeeId>) : AggregateRoot<Company, CompanyId> {
    fun addEmployee(employeeId: EmployeeId): Company {
        return copy(employees = employees + employeeId)
    }
}

class CompanyId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<CompanyId>(::CompanyId)
}
