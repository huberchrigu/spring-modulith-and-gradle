package tech.chrigu.spring.modulith.portfolio.skill

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowDeletedEvent
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowUpdatedEvent
import tech.chrigu.spring.modulith.portfolio.skill.mongo.SkillRepository

@Service
class SkillService(private val skillRepository: SkillRepository, private val coroutineScope: CoroutineScope) {
    suspend fun findByName(name: String): Skill? {
        return skillRepository.findByName(name)
    }

    suspend fun create(name: String): Skill {
        return skillRepository.save(Skill(SkillId.newId(), name))
    }

    @EventListener
    fun on(e: KnowHowUpdatedEvent) = coroutineScope.launch {
        skillRepository.save(e.knowHow.toSkill())
    }

    @EventListener
    fun on(e: KnowHowDeletedEvent) = coroutineScope.launch {
        skillRepository.delete(e.knowHow.toSkill())
    }

    suspend fun clear() {
        skillRepository.deleteAll()
    }
    private fun KnowHow.toSkill() = Skill(id.toSkillId(), title)
    private fun KnowHowId.toSkillId() = SkillId(id)
}
