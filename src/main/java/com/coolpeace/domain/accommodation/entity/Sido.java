package com.coolpeace.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "sido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Accommodation> accommodation;

    private Sido(String name) {
        this.name = name;
    }

    public static Sido from(String name) {
        return new Sido(name);
    }
}
