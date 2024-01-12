package com.coolpeace.core.domain.accommodation.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sido {

    @Id
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "sido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Sigungu> sigungus;

    @OneToMany(mappedBy = "sido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Accommodation> accommodation;

}
