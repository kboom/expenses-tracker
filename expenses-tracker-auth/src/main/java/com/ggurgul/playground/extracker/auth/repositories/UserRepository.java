package com.ggurgul.playground.extracker.auth.repositories;

import com.ggurgul.playground.extracker.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);

}
