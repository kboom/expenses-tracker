package com.ggurgul.playground.extracker.auth.controllers

import com.ggurgul.playground.extracker.auth.exceptions.UserNotFoundException
import com.ggurgul.playground.extracker.auth.models.User
import com.ggurgul.playground.extracker.auth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.websocket.server.PathParam

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/:username")
    fun getUserByUsername(@PathParam("username") username: String): User {
        return userRepository.findByUsername(username).orElseThrow { UserNotFoundException() }
    }

}
