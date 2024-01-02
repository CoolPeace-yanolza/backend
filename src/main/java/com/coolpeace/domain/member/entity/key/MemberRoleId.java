package com.coolpeace.domain.member.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoleId implements Serializable {
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    public static MemberRoleId of(Long memberId, Long roleId) {
        return new MemberRoleId(memberId, roleId);
    }
}
