package no.spk.beholdning.ws

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object TestUtil {

    private val mapper = ObjectMapper().also {
        it.registerModule(JavaTimeModule())
        it.registerKotlinModule()
        it.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    fun Any.toJson() = mapper.writeValueAsString(this)
}
