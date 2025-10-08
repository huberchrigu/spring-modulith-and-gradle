package tech.chrigu.spring.modulith.hr.knowhow.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.chrigu.spring.modulith.hr.knowhow.KnowHowService
import tech.chrigu.spring.modulith.hr.shared.web.HrApi
import java.net.URI

@RestController
@RequestMapping("${HrApi.BASE_URI}/know-how")
internal class KnowHowRestController(private val knowHowService: KnowHowService) {
    @PostMapping
    suspend fun create(@RequestBody body: CreateBody) = knowHowService.create(body.title)
        .let { ResponseEntity.created(URI.create("/hr/know-how/${it.id}")).body(it) }

    data class CreateBody(val title: String)
}
