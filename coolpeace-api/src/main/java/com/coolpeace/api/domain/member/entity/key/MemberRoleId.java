package com.coolpeace.api.domain.member.entity.key;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoleId implements Serializable {
    private Long memberId;
    private Long roleId;

    public static MemberRoleId from(Long memberId, Long roleId) {
        return new MemberRoleId(memberId, roleId);
    }
}
