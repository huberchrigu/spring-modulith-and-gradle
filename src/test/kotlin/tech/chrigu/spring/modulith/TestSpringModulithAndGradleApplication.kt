package tech.chrigu.spring.modulith

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<SpringModulithAndGradleApplication>()
        .with(TestcontainersConfiguration::class, TestDataLoader::class)
        .run(*args)
}
