package tech.chrigu.spring.modulith.hr.knowhow

import org.springframework.stereotype.Service
import tech.chrigu.spring.modulith.hr.knowhow.mongo.KnowHowRepository

@Service
class KnowHowService(private val knowHowRepository: KnowHowRepository) {
    suspend fun exists(skill: KnowHowId): Boolean {
        return knowHowRepository.existsById(skill)
    }

    suspend fun create(title: String) = knowHowRepository.save(KnowHow(KnowHowId.newId(), title))
    suspend fun clear() {
        knowHowRepository.deleteAll()
    }

    suspend fun findByTitle(name: String): KnowHow? {
        return knowHowRepository.findByTitle(name)
    }
}
