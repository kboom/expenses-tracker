package com.toptal.ggurgul.timezones.security

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

    private val LOG = LogFactory.getLog(this.javaClass)

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Value("\${jwt.header}")
    private val tokenHeader: String? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val authToken = request.getHeader(this.tokenHeader)
            if(authToken != null) {
                val username = jwtTokenUtil!!.getUsernameFromToken(authToken)

                LOG.info("checking authentication for user " + username)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = this.userDetailsService!!.loadUserByUsername(username)
                    if (jwtTokenUtil.validateToken(authToken, userDetails)!!) {
                        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        logger.info("authenticated user $username, setting security context")
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }

        } catch(e: Exception) {
            LOG.debug("Could not authenticate user", e)
        } finally {
            chain.doFilter(request, response)
        }
    }
}