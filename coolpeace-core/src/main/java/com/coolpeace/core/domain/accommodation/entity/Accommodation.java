package com.coolpeace.core.domain.accommodation.entity;

import com.coolpeace.core.common.BaseTimeEntity;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "sido")
    private Sido sido;

    @ManyToOne
    @JoinColumn(name = "sigungu")
    private Sigungu sigungu;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
}
