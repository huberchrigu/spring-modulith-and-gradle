package tech.chrigu.spring.modulith.portfolio.shared

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import tech.chrigu.spring.modulith.shared.AbstractIdentifier

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = [AbstractIdentifier::class])
class ImportSharedConfiguration
