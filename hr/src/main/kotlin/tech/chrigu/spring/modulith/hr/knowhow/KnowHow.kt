package tech.chrigu.spring.modulith.hr.knowhow

data class KnowHow(private val id: KnowHowId?, private val title: String)

@JvmInline
value class KnowHowId(private val id: String)
