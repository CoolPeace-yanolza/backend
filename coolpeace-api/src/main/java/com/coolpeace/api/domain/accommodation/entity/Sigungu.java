package com.coolpeace.api.domain.accommodation.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Sigungu {

    @Id
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "sido")
    private Sido sido;

    @OneToMany(mappedBy = "sigungu", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Accommodation> accommodations = new ArrayList<>();

}
