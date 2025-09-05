package no.spk.beholdning.ws.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.spk.felles.logging.BodyMasker
import no.spk.felles.outbound.OutboundHeaderInterceptor
import no.spk.felles.outbound.OutboundHeaderProvider
import no.spk.felles.outbound.OutboundLoggingInterceptor
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient

class BeholdningHttpClientBuilder {

    private var baseUrl: String? = null
    private var outboundHeaderProvider: OutboundHeaderProvider? = null

    fun withBaseUrl(baseUrl: String): BeholdningHttpClientBuilder {
        this.baseUrl = baseUrl
        return this
    }

    fun withHeaderProvider(headerProvider: OutboundHeaderProvider): BeholdningHttpClientBuilder {
        this.outboundHeaderProvider = headerProvider
        return this
    }

    fun build(): BeholdningHttpClient {
        val messageConverter = MappingJackson2HttpMessageConverter(
            jacksonObjectMapper()
                .registerModules(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        )
        val baseUrl = baseUrl ?: throw IllegalArgumentException("Base URL is required")
        val outboundHeaderProvider = outboundHeaderProvider
            ?: throw IllegalArgumentException("Header provider is required")
        return BeholdningHttpClient(
            client = RestClient.builder()
                .baseUrl("$baseUrl/api")
                .messageConverters(listOf(messageConverter))
                .requestInterceptor(OutboundHeaderInterceptor(outboundHeaderProvider))
                .requestInterceptor(
                    OutboundLoggingInterceptor.builder()
                        .withPayloadLogging(
                            BodyMasker.builder()
                                .iHaveCheckedAndConfirmThatThereAreNoSensitiveFields()
                                .build()
                        )
                        .build()
                )
                .build()
        )
    }
}