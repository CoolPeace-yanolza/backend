package com.coolpeace.domain.member.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberRole> roles = new ArrayList<>();

    @Column(nullable = false)
    private final boolean isDeleted = false;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Accommodation> accommodations;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Coupon> coupons;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ServiceTerm> serviceTerms;

    private Member(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name =name;
        this.roles.add(MemberRole.from(this, role));
    }

    public static Member from(String email, String password, String name, Role role) {
        return new Member(email, password, name, role);
    }
}
