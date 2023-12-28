package com.coolpeace.domain.member.entity;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String role;

    private boolean isDeleted;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Accommodation> accommodationList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Coupon> couponList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ServiceTerm> serviceTermList;

}
