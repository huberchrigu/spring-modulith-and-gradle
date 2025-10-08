package tech.chrigu.spring.modulith.shared.web

import org.jmolecules.spring.IdentifierToPrimitivesConverter
import org.jmolecules.spring.PrimitivesToIdentifierConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.format.WebConversionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.ConversionService
import org.springframework.format.FormatterRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.util.function.Supplier

/**
 * Supports [org.jmolecules.ddd.types.Identifier] bindings. If needed, [org.jmolecules.ddd.annotation.Association]s could be supported as well (see in the private method below).
 *
 * The jackson mapping is configured in [org.jmolecules.jackson.config.JMoleculesJacksonAutoConfiguration].
 * And optionally for associations, in [org.jmolecules.spring.config.JMoleculesSpringJacksonAutoConfiguration].
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
internal class JMoleculesWebConfiguration {
    @Bean
    fun jMoleculesWebMvcConfigurer(): WebFluxConfigurer {
        return object : WebFluxConfigurer {
            override fun addFormatters(registry: FormatterRegistry) {
                registerConverters(registry as WebConversionService)
            }
        }
    }

    /**
     * The association converters are currently commented out because we do not use associations yet.
     */
    private fun registerConverters(conversionService: WebConversionService) {
        val supplier: Supplier<ConversionService> = Supplier { conversionService }

        val identifierToPrimitives = IdentifierToPrimitivesConverter(supplier)
        val primitivesToIdentifier = PrimitivesToIdentifierConverter(supplier)

        conversionService.addConverter(identifierToPrimitives)
        conversionService.addConverter(primitivesToIdentifier)
//        conversionService.addConverter(PrimitivesToAssociationConverter(primitivesToIdentifier))
//        conversionService.addConverter(AssociationToPrimitivesConverter(identifierToPrimitives))
    }
}
