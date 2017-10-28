package com.toptal.ggurgul.timezones.security

import com.toptal.ggurgul.timezones.domain.services.TimeService
import io.jsonwebtoken.ExpiredJwtException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.util.DateUtil
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils
import java.util.*

class JwtTokenUtilTest {

    @Mock
    private val timeServiceMock: TimeService? = null

    @InjectMocks
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 3600L) // one hour
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "mySecret")
    }

    @Test
    @Throws(Exception::class)
    fun testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.yesterday())
                .thenReturn(DateUtil.now())

        val token = createToken()
        val laterToken = createToken()

        assertThat(token).isNotEqualTo(laterToken)
    }

    @Test
    @Throws(Exception::class)
    fun getUsernameFromToken() {
        `when`(timeServiceMock!!.now()).thenReturn(DateUtil.now())

        val token = createToken()

        assertThat(jwtTokenUtil!!.getUsernameFromToken(token)).isEqualTo(TEST_USERNAME)
    }

    @Test
    @Throws(Exception::class)
    fun getCreatedDateFromToken() {
        val now = DateUtil.now()
        `when`(timeServiceMock!!.now()).thenReturn(now)

        val token = createToken()

        assertThat(jwtTokenUtil!!.getIssuedAtDateFromToken(token)).isInSameMinuteWindowAs(now)
    }

    @Test
    @Throws(Exception::class)
    fun getExpirationDateFromToken() {
        val now = DateUtil.now()
        `when`(timeServiceMock!!.now()).thenReturn(now)
        val token = createToken()

        val expirationDateFromToken = jwtTokenUtil!!.getExpirationDateFromToken(token)
        assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(3600000L, within(1000L))
    }

    @Test
    @Throws(Exception::class)
    fun getAudienceFromToken() {
        `when`(timeServiceMock!!.now()).thenReturn(DateUtil.now())
        val token = createToken()

        assertThat(jwtTokenUtil!!.getAudienceFromToken(token)).isEqualTo(JwtTokenUtil.AUDIENCE_WEB)
    }

    @Test(expected = ExpiredJwtException::class)
    @Throws(Exception::class)
    fun expiredTokenCannotBeRefreshed() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.yesterday())
        val token = createToken()
        jwtTokenUtil!!.canTokenBeRefreshed(token, DateUtil.tomorrow())
    }

    @Test
    @Throws(Exception::class)
    fun changedPasswordCannotBeRefreshed() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.now())
        val token = createToken()
        assertThat(jwtTokenUtil!!.canTokenBeRefreshed(token, DateUtil.tomorrow())).isFalse()
    }

    @Test
    fun notExpiredCanBeRefreshed() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.now())
        val token = createToken()
        assertThat(jwtTokenUtil!!.canTokenBeRefreshed(token, DateUtil.yesterday())).isTrue()
    }

    @Test
    @Throws(Exception::class)
    fun canRefreshToken() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.now())
                .thenReturn(DateUtil.tomorrow())
        val firstToken = createToken()
        val refreshedToken = jwtTokenUtil!!.refreshToken(firstToken)
        val firstTokenDate = jwtTokenUtil.getIssuedAtDateFromToken(firstToken)
        val refreshedTokenDate = jwtTokenUtil.getIssuedAtDateFromToken(refreshedToken)
        assertThat(firstTokenDate).isBefore(refreshedTokenDate)
    }

    @Test
    @Throws(Exception::class)
    fun canValidateToken() {
        `when`(timeServiceMock!!.now())
                .thenReturn(DateUtil.now())
        val userDetails = mock(JwtUser::class.java)
        `when`(userDetails.username).thenReturn(TEST_USERNAME)

        val token = createToken()
        assertThat(jwtTokenUtil!!.validateToken(token, userDetails)).isTrue()
    }

    private fun createToken(): String {
        val device = DeviceDummy()
        device.isNormal = true

        return jwtTokenUtil!!.generateToken(UserDetailsDummy(TEST_USERNAME), device)
    }

    companion object {

        private val TEST_USERNAME = "testUser"
    }

}