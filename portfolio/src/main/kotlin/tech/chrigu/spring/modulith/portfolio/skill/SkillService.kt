package tech.chrigu.spring.modulith.portfolio.skill

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.portfolio.skill.mongo.SkillRepository

@Service
internal class SkillService(private val skillRepository: SkillRepository) {
    suspend fun findByName(name: String): Skill? {
        return skillRepository.findByName(name)
    }

    suspend fun create(name: String): Skill {
        return skillRepository.save(Skill(SkillId.newId(), name))
    }
}
