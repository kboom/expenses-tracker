package com.ggurgul.playground.extracker.auth.services

import org.springframework.stereotype.Component
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.SecureRandom
import java.util.*

@Component
class UserCodeTranslator(
        private val secureRandom: SecureRandom = SecureRandom()
) {

    fun generateFor(data: String): String {
        val secret = generateRandomHexToken(16)
        val toEncode = "$data:$secret";
        return Base64.getEncoder().encodeToString(toEncode.toByteArray());
    }

    fun readFrom(code: String): String {
        val decoded = Base64.getDecoder().decode(code)
        return decoded.toString(Charset.forName("UTF-8"))
    }

    private fun generateRandomHexToken(byteLength: Int): String {
        val token = ByteArray(byteLength)
        secureRandom.nextBytes(token)
        return BigInteger(1, token).toString(16)
    }

}