package com.coolpeace.global.jwt.security;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.entity.type.RoleType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class JwtPrincipal implements UserDetails {
    private final String memberId;
    private final String memberEmail;
    private final boolean enabled;
    private final boolean isGuestUser;
    private final List<GrantedAuthority> authorities;

    public JwtPrincipal(String memberId, String memberEmail, boolean enabled,
                        List<GrantedAuthority> authorities, boolean isGuestUser) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.enabled = enabled;
        this.authorities = authorities;
        this.isGuestUser = isGuestUser;
    }

    public static JwtPrincipal from(Member member) {
        List<String> roles = member.getRoles().stream()
                .map(memberRole -> memberRole.getRole().getRoleType().getValue())
                .toList();
        return new JwtPrincipal(
                String.valueOf(member.getId()),
                member.getEmail(),
                !member.isDeleted(),
                AuthorityUtils.createAuthorityList(roles),
                roles.contains(RoleType.USER.getValue())
        );
    }
    public static JwtPrincipal emptyPrincipal() {
        return new JwtPrincipal(null, null, false, AuthorityUtils.NO_AUTHORITIES, false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.memberEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
