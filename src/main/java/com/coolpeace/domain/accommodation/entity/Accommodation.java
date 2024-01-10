package com.coolpeace.domain.accommodation.entity;

import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.global.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    public Accommodation(String name, Sido sido, Sigungu sigungu, String address, Member member) {
        this.name = name;
        this.sido = sido;
        this.sigungu = sigungu;
        this.address = address;
        this.member = member;
    }

    public static Accommodation from(String name, Sido sido, Sigungu sigungu, String address, Member member) {
        return new Accommodation(name, sido, sigungu, address, member);
    }

    public Accommodation(Long id, String name, String address, Member member) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.member = member;
    }
}
