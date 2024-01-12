package com.coolpeace.core.domain.member.entity.key;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoleId implements Serializable {
    private Long memberId;
    private Long roleId;

    public static MemberRoleId from(Long memberId, Long roleId) {
        return new MemberRoleId(memberId, roleId);
    }
}
