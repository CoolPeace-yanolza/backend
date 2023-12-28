package com.coolpeace.domain.coupon.entity;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private DiscountType discountType;

    private int maxDiscount;

    private MemberType memberType;

    private RoomType roomType;

    private UseCase useCase;

    private int usePay;

    private String useDays;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime expiration;

    private Boolean isExposure;

    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
