package com.coolpeace.domain.accommodation.service;

import com.coolpeace.domain.accommodation.dto.response.AccommodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.dto.response.WrapAccommodationResponse;
import com.coolpeace.domain.accommodation.dto.response.WrapRoomResponse;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.global.jwt.security.JwtPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccomodationService {

    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Cacheable(value = "accommodation", key = "#jwtPrincipal.toString()",cacheManager = "contentCacheManager")
    public WrapAccommodationResponse getAccommodations(JwtPrincipal jwtPrincipal) {

        Long memberId = Long.parseLong(jwtPrincipal.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return WrapAccommodationResponse
            .from(accommodationRepository.findAllByMember(member)
                .stream()
                .map(AccommodationResponse::fromEntity)
                .toList());
    }

    @Cacheable(value = "rooms", key = "#accommodationId",cacheManager = "contentCacheManager")
    public WrapRoomResponse getRooms(JwtPrincipal jwtPrincipal, Long accommodationId) {

        Long memberId = Long.parseLong(jwtPrincipal.getMemberId());
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);

        return WrapRoomResponse
            .from(roomRepository.findAllByAccommodation(accommodation)
            .stream()
            .map(RoomResponse::fromEntity)
            .toList());
    }
}
