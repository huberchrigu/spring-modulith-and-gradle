package tech.chrigu.spring.modulith.portfolio.service

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.portfolio.service.mongo.ServiceRepository
import tech.chrigu.spring.modulith.portfolio.skill.SkillId

@Service
class ServiceService(private val serviceRepository: ServiceRepository) {
    suspend fun create(title: String, description: String, requiredSkills: List<SkillId>) = serviceRepository.save(Service(ServiceId.newId(), title, description, requiredSkills))
    suspend fun addSkill(id: ServiceId, skillId: SkillId) = serviceRepository.findById(id)
        ?.add(skillId)
        ?.let { serviceRepository.save(it) }

    suspend fun removeSkill(id: ServiceId, skillId: SkillId) = serviceRepository.findById(id)
        ?.remove(skillId)
        ?.let { serviceRepository.save(it) }

    fun findByTitle(title: String) = serviceRepository.findByTitle(title)
}