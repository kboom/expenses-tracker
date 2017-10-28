package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.services.TimeService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.function.Function
import kotlin.streams.toList

@Component
class JwtTokenUtil : Serializable {

    @Autowired
    private var timeService: TimeService? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.subject })
    }

    fun getIssuedAtDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.issuedAt })
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.expiration })
    }

    fun getAudienceFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.audience })
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(timeService!!.now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created.before(lastPasswordReset)
    }

    private fun generateAudience(device: Device): String {
        var audience = AUDIENCE_UNKNOWN
        if (device.isNormal) {
            audience = AUDIENCE_WEB
        } else if (device.isTablet) {
            audience = AUDIENCE_TABLET
        } else if (device.isMobile) {
            audience = AUDIENCE_MOBILE
        }
        return audience
    }

    private fun ignoreTokenExpiration(token: String): Boolean? {
        val audience = getAudienceFromToken(token)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
    }

    fun generateToken(userDetails: UserDetails, device: Device): String {
        val claims = HashMap<String, Any>()

        claims.put("authorities", userDetails.authorities
                .stream()
                .map { it.authority }
                .toList()
        )

        return doGenerateToken(claims, userDetails.username, generateAudience(device))
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String, audience: String): String {
        val createdDate = timeService!!.now()
        val expirationDate = Date(createdDate.time + expiration!! * 1000)

        println("doGenerateToken " + createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean? {
        val created = getIssuedAtDateFromToken(token)
        return (!isCreatedBeforeLastPasswordReset(created, lastPasswordReset)) && (!isTokenExpired(token) || ignoreTokenExpiration(token)!!)
    }

    fun refreshToken(token: String): String {
        val claims = getAllClaimsFromToken(token)
        claims.issuedAt = timeService!!.now()
        return doRefreshToken(claims)
    }

    fun doRefreshToken(claims: Claims): String {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean? {
        val user = userDetails as JwtUser
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        //final Date expiration = getExpirationDateFromToken(token);
        return username == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate)
    }

    companion object {

        private const val serialVersionUID = -3301605591108950415L

        val CLAIM_KEY_USERNAME = "sub"
        val CLAIM_KEY_AUDIENCE = "aud"
        val CLAIM_KEY_CREATED = "iat"

        val AUDIENCE_UNKNOWN = "unknown"
        val AUDIENCE_WEB = "web"
        val AUDIENCE_MOBILE = "mobile"
        val AUDIENCE_TABLET = "tablet"
    }

}