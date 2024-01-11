package com.coolpeace.domain.accommodation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<Accommodation> findAllBySigunguName(String name);

}
