package com.example.rbac.security;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.rbac.entity.User;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Formulates list incorporating both mapping structural concepts: Roles AND Permissions
        return user.getRoles().stream()
            .flatMap(role -> Stream.concat(
                Stream.of(new SimpleGrantedAuthority(role.getName())),
                role.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getName()))
            ))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return user.getPassword(); }
    @Override
    public String getUsername() { return user.getUsername(); }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}