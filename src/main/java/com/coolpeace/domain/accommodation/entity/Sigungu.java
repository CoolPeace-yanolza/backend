package com.coolpeace.domain.accommodation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sigungu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "sido")
    private Sido sido;

    @OneToMany(mappedBy = "sigungu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Accommodation> accommodations = new ArrayList<>();

    private Sigungu(String name) {
        this.name = name;
    }

    public static Sigungu from(String name) {
        return new Sigungu(name);
    }
}
