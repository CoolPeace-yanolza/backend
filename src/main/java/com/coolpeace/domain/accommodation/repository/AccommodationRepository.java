package com.coolpeace.domain.accommodation.repository;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
