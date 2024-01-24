package com.coolpeace.domain.data.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenerateAccommodationRepository extends JpaRepository<Accommodation, Long> {

}
