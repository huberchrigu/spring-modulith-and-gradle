package tech.chrigu.spring.modulith.hr.knowhow.mongo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId

internal interface KnowHowRepository : CoroutineCrudRepository<KnowHow, KnowHowId> {
    suspend fun findByTitle(name: String): KnowHow?
}
