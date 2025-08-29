package tech.chrigu.spring.modulith.hr.company

import org.jmolecules.ddd.types.AggregateRoot
import tech.chrigu.spring.modulith.hr.employee.EmployeeId
import tech.chrigu.spring.modulith.hr.shared.AbstractIdentifier
import tech.chrigu.spring.modulith.hr.shared.IdentifierObject
import java.util.UUID

data class Company(override val id: CompanyId, val name: String, val employees: List<EmployeeId>) : AggregateRoot<Company, CompanyId> {
    fun addEmployee(employeeId: EmployeeId): Company {
        return copy(employees = employees + employeeId)
    }
}

class CompanyId(id: UUID) : AbstractIdentifier(id) {
    companion object : IdentifierObject<CompanyId>(::CompanyId)
}
