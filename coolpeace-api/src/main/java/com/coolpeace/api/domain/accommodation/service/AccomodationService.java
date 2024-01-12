package com.coolpeace.api.domain.accommodation.service;

import com.coolpeace.api.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.api.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.core.domain.accommodation.entity.Accommodation;
import com.coolpeace.core.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.core.domain.member.entity.Member;
import com.coolpeace.core.domain.member.repository.MemberRepository;
import com.coolpeace.core.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccomodationService {

    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    public List<AccomodationResponse> getAccommodations(JwtPrincipal jwtPrincipal) {

        Long memberId = Long.parseLong(jwtPrincipal.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return accommodationRepository.findAllByMember(member)
            .stream()
            .map(AccomodationResponse::fromEntity)
            .toList();
    }

    public List<RoomResponse> getRooms(JwtPrincipal jwtPrincipal, Long accommodationId) {

        Long memberId = Long.parseLong(jwtPrincipal.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);

        return roomRepository.findAllByAccommodation(accommodation)
            .stream()
            .map(RoomResponse::fromEntity)
            .toList();
    }
}
