package com.coolpeace.api.domain.accommodation.service;

import com.coolpeace.api.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.api.domain.accommodation.entity.Accommodation;
import com.coolpeace.api.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.api.domain.member.entity.Member;
import com.coolpeace.api.global.jwt.security.JwtPrincipal;
import com.coolpeace.api.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.api.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.api.domain.member.exception.MemberNotFoundException;
import com.coolpeace.api.domain.member.repository.MemberRepository;
import com.coolpeace.api.domain.room.repository.RoomRepository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
