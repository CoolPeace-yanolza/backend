package com.coolpeace.core.domain.accommodation.repository;

import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<Accommodation> findAllBySigunguName(String name);

}
