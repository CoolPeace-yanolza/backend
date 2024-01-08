package com.coolpeace.domain.accommodation.service;

import com.coolpeace.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccomodationService {

    private final AccommodationRepository accommodationRepository;

    public List<AccomodationResponse> getAccommodations(JwtPrincipal jwtPrincipal) {

        Integer memberId = Integer.parseInt(jwtPrincipal.getMemberId());

        List<Accommodation> accommodations
            = accommodationRepository.findAllByMemberId(memberId);

        List<AccomodationResponse> accomodationResponses = new ArrayList<>();
        for(Accommodation accommodation : accommodations){
            accomodationResponses.add(AccomodationResponse.fromEntity(accommodation));
        }

        return accomodationResponses;
    }
}
