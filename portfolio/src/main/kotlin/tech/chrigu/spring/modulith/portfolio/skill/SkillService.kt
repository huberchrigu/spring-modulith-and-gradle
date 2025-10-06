package tech.chrigu.spring.modulith.portfolio.skill

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.modulith.ApplicationModuleListener
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowDeletedEvent
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.skill.mongo.SkillRepository

@Service
internal class SkillService(private val skillRepository: SkillRepository, private val coroutineScope: CoroutineScope) {
    suspend fun findByName(name: String): Skill? {
        return skillRepository.findByName(name)
    }

    suspend fun create(name: String): Skill {
        return skillRepository.save(Skill(SkillId.newId(), name))
    }

    @ApplicationModuleListener
    fun on(e: KnowHowUpdatedEvent) = coroutineScope.launch {
        skillRepository.save(e.knowHow.toSkill())
    }

    @ApplicationModuleListener
    fun on(e: KnowHowDeletedEvent) = coroutineScope.launch {
        skillRepository.delete(e.knowHow.toSkill())
    }

    private fun KnowHow.toSkill() = Skill(id.toSkillId(), title)
    private fun KnowHowId.toSkillId() = SkillId(id)
}
