package tech.chrigu.spring.modulith.portfolio.service

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.portfolio.service.mongo.ServiceRepository
import tech.chrigu.spring.modulith.portfolio.skill.SkillId

@Service
internal class ServiceService(private val serviceRepository: ServiceRepository) {
    suspend fun create(title: String, description: String, requiredSkills: List<SkillId>) = serviceRepository.save(Service(ServiceId.newId(), title, description, requiredSkills))
    fun findByTitle(title: String) = serviceRepository.findByTitle(title)
}