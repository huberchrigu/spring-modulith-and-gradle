package tech.chrigu.spring.modulith.hr.knowhow

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.knowhow.mongo.KnowHowRepository

@Service
internal class KnowHowService(private val knowHowRepository: KnowHowRepository, private val applicationEventPublisher: ApplicationEventPublisher) {
    suspend fun exists(skill: KnowHowId): Boolean {
        return knowHowRepository.existsById(skill)
    }

    suspend fun create(title: String) = knowHowRepository.save(KnowHow(KnowHowId.newId(), title))
        .also { applicationEventPublisher.publishEvent(KnowHowUpdatedEvent(it)) }

    suspend fun clear() {
        knowHowRepository.findAll()
            .collect {
                knowHowRepository.delete(it)
                applicationEventPublisher.publishEvent(KnowHowDeletedEvent(it))
            }
    }

    suspend fun findByTitle(name: String): KnowHow? {
        return knowHowRepository.findByTitle(name)
    }
}

data class KnowHowUpdatedEvent(val knowHow: KnowHow) : ApplicationEvent(knowHow)
data class KnowHowDeletedEvent(val knowHow: KnowHow) : ApplicationEvent(knowHow)