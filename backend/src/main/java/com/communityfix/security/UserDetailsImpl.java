package com.communityfix.security;

import com.communityfix.model.User;
import com.communityfix.model.enums.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String fullName;
    private final String email;
    private final String password;
    private final UserRole role;
    private final Boolean approved;
    private final String flatNumber;
    private final String block;
    private final Integer reputation;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.approved = user.getApproved();
        this.flatNumber = user.getFlatNumber();
        this.block = user.getBlock();
        this.reputation = user.getReputation();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return approved != null && approved;
    }
}
