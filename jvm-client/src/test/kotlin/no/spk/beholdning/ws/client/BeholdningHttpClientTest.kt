package no.spk.beholdning.ws.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.containing
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import no.spk.beholdning.ws.TestUtil.toJson
import no.spk.beholdning.ws.model.SampleStore.exampleBeregnBeholdningRequestV5
import no.spk.beholdning.ws.model.SampleStore.exampleBeregnBeholdningResponseV5
import no.spk.felles.outbound.OutboundHeaderProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.client.HttpClientErrorException.BadRequest

internal class BeholdningHttpClientTest {

    private val wiremock = WireMockServer(
        WireMockConfiguration.options()
            .dynamicPort()
            .http2PlainDisabled(true) // workaround for https://github.com/jetty/jetty.project/issues/11588
            .usingFilesUnderClasspath("wiremock")
    ).also { it.start() }

    private val client = BeholdningHttpClientBuilder()
        .withBaseUrl(wiremock.baseUrl())
        .withHeaderProvider(object : OutboundHeaderProvider {
            override fun getApplicationId() = "junit"
            override fun getCorrelationId() = "f8c"
            override fun getRequestOrigin() = "junit"
            override fun getAuthorization() = "Token secret"
        })
        .build()

    @BeforeEach
    fun setup() {
        wiremock.resetAll()
    }

    @Nested
    inner class BeregnBeholdningV5 {

        @Test
        fun `Beregner beholdning`() {
            val result = client.beregnBeholdningV5(exampleBeregnBeholdningRequestV5())
            result shouldBe exampleBeregnBeholdningResponseV5()
            wiremock.verify(
                postRequestedFor(urlPathEqualTo("/api/beholdning/beregn"))
                    .withHeader("X-Application-Id", equalTo("junit"))
                    .withHeader("X-Correlation-Id", equalTo("f8c"))
                    .withHeader("X-Request-Origin", equalTo("junit"))
                    .withHeader("Authorization", equalTo("Token secret"))
                    .withHeader("Content-Type", containing("application/vnd.spk.v5+json"))
                    .withHeader("Accept", containing("application/vnd.spk.v5+json"))
                    .withRequestBody(equalToJson(exampleBeregnBeholdningRequestV5().toJson()))
            )
        }

        @Test
        fun `Tolererer en response med Bad Request`() {
            wiremock.stubFor(
                post(urlPathEqualTo("/api/beholdning/beregn"))
                    .willReturn(
                        aResponse()
                            .withStatus(400)
                            .withBodyFile("bad-request.json")
                            .withHeader("Content-Type", "application/problem+json")
                    )
            )
            val e = shouldThrow<BadRequest> { client.beregnBeholdningV5(exampleBeregnBeholdningRequestV5()) }
            e.message shouldContain "Ugyldig input"
        }
    }
}
