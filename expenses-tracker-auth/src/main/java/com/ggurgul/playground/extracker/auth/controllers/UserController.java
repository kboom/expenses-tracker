package com.ggurgul.playground.extracker.auth.controllers;

import com.ggurgul.playground.extracker.auth.models.User;
import com.ggurgul.playground.extracker.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/:username")
    public User getUserByUsername(@PathParam("username") String username) {
        return userRepository.findOneByUsername(username);
    }

}
