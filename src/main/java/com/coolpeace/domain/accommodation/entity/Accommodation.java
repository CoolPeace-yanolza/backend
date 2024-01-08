package com.coolpeace.domain.accommodation.entity;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "id")
    private Sido sido;

    @OneToOne
    @JoinColumn(name = "id")
    private Sigungu sigungu;

    private String addressDetail;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
