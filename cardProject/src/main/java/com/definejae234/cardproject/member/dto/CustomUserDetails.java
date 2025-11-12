package com.definejae234.cardproject.member.dto;

import com.definejae234.cardproject.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails , OAuth2User {
    private final Member loggedMember;
    private Map<String,Object> oauth2UserInfo;

    public CustomUserDetails(Member loggedMember) {
        this.loggedMember = loggedMember;
    }
    public CustomUserDetails(Member loggedMember, Map<String, Object> oauth2UserInfo) {
        this.loggedMember = loggedMember;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(loggedMember.getRole()));
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public String getPassword() {

        return loggedMember.getUserPW();
    }

    @Override
    public String getUsername() {
        return loggedMember.getUserID();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public String getName() {
        return oauth2UserInfo.get("name").toString();
    }
}
