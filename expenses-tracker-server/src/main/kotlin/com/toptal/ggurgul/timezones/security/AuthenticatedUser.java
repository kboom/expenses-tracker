package com.toptal.ggurgul.timezones.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

public interface AuthenticatedUser {

    Long getId();

    String getUsername();

    boolean isAccountNonExpired();

    boolean isAccountNonLocked();

    boolean isCredentialsNonExpired();

    String getEmail();

    String getPassword();

    Collection<? extends GrantedAuthority> getAuthorities();

    boolean isEnabled();

    Date getLastPasswordResetDate();

}
