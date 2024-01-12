package com.coolpeace.core.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sido")
    private Sido sido;

    @OneToMany(mappedBy = "sigungu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Accommodation> accommodations = new ArrayList<>();

    private Sigungu(String name, Sido sido) {
        this.name = name;
        this.sido = sido;
    }

    public static Sigungu from(String name, Sido sido) {
        return new Sigungu(name, sido);
    }
}
