package com.coolpeace.domain.member.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_USER"),
    OWNER("ROLE_OWNER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}
