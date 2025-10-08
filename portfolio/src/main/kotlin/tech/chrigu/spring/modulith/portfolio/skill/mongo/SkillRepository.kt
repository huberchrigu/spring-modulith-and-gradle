package tech.chrigu.spring.modulith.portfolio.skill.mongo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.portfolio.skill.Skill
import tech.chrigu.spring.modulith.portfolio.skill.SkillId

interface SkillRepository : CoroutineCrudRepository<Skill, SkillId> {
    suspend fun findByName(name: String): Skill?
}
