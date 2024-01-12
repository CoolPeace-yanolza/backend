package com.coolpeace.api.global.factory;

import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.entity.Role;
import com.coolpeace.core.domain.member.entity.type.RoleType;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberTestBuilder {
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Faker faker = new Faker();
    private String password;
    private boolean isEncodedPassword = false;
    private Role role = Role.of(RoleType.OWNER);

    public MemberTestBuilder password(String password) {
        this.password = password;
        return this;
    }

    public MemberTestBuilder encoded() {
        this.isEncodedPassword = true;
        return this;
    }

    public MemberTestBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public MemberTestBuilder randomRole() {
        this.role = Role.of(RoleType.values()[faker.random().nextInt(0, 2)]);
        return this;
    }

    public Member build() {
        String memberPass = password == null ? faker.internet().password() : password;
        if (isEncodedPassword) {
            memberPass = passwordEncoder.encode(memberPass);
        }

        return Member.from(
                faker.internet().emailAddress(),
                memberPass,
                faker.name().firstName(),
                role
        );
    }
}
