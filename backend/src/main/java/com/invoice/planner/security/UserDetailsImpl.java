package com.invoice.planner.security;

import com.invoice.planner.entity.User;
import com.invoice.planner.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implémentation personnalisée de UserDetails pour notre entité User
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final UUID trackingId;
    private final String email;
    private final boolean isActive;
    private final boolean isLocked;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            UUID trackingId,
            String email,
            String password,
            boolean isActive,
            boolean isLocked,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.trackingId = trackingId;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.isLocked = isLocked;
        this.authorities = authorities;
    }

    /**
     * Crée une instance de UserDetailsImpl à partir d'une entité User
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getTrackingId(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                user.isLocked(),
                authorities);
    }

    public Long getId() {
        return id;
    }

    public UUID getTrackingId() {
        return trackingId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Nous utilisons l'email comme identifiant
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Nous ne gérons pas l'expiration des comptes
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Nous ne gérons pas l'expiration des identifiants
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
