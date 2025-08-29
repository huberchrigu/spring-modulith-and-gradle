package tech.chrigu.spring.modulith.shared.mongo

import org.jmolecules.ddd.types.Identifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.util.UUID

@Configuration(proxyBeanMethods = false)
class JMoleculesMongoConfiguration {
    @Bean
    fun mongoCustomConversions() = MongoCustomConversions.create { configurer ->
        configurer.registerConverters(listOf(IdentityToUuidConverter(), UuidToIdentityConverter()))
    }

    class IdentityToUuidConverter : Converter<Identifier, UUID> {
        override fun convert(value: Identifier): UUID {
            return UUID.fromString(value.toString())
        }
    }

    class UuidToIdentityConverter : GenericConverter {
        override fun getConvertibleTypes(): Set<GenericConverter.ConvertiblePair> {
            return setOf(GenericConverter.ConvertiblePair(UUID::class.java, Identifier::class.java))
        }

        override fun convert(
            source: Any?,
            sourceType: TypeDescriptor,
            targetType: TypeDescriptor
        ): Any? {
            if (source == null || source !is UUID) {
                return null
            } else {
                val type = targetType.type
                val constructor = type.constructors.first()
                return constructor.newInstance(source) as Identifier
            }
        }
    }
}
