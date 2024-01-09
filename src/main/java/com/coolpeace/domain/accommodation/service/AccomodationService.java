package com.coolpeace.domain.accommodation.service;

import com.coolpeace.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import java.util.ArrayList;
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
        Member member =memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);

        return roomRepository.findAllByAccommodation(accommodation)
            .stream()
            .map(RoomResponse::fromEntity)
            .toList();
    }
}
