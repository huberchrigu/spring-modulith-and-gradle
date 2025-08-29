package tech.chrigu.spring.modulith.hr.knowhow.mongo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import tech.chrigu.spring.modulith.hr.knowhow.KnowHow
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowId

interface KnowHowRepository : CoroutineCrudRepository<KnowHow, KnowHowId>
