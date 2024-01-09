package com.coolpeace.domain.accommodation.service;

import com.coolpeace.domain.accommodation.dto.response.AccomodationResponse;
import com.coolpeace.domain.accommodation.dto.response.RoomResponse;
import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
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

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    public List<AccomodationResponse> getAccommodations(JwtPrincipal jwtPrincipal) {

        Integer memberId = Integer.parseInt(jwtPrincipal.getMemberId());

        List<Accommodation> accommodations
            = accommodationRepository.findAllByMemberId(memberId);

        List<AccomodationResponse> accomodationResponses = new ArrayList<>();
        for (Accommodation accommodation : accommodations) {
            accomodationResponses.add(AccomodationResponse.fromEntity(accommodation));
        }

        return accomodationResponses;
    }

    public List<RoomResponse> getRooms(JwtPrincipal jwtPrincipal, Long accommodationId) {

        Integer memberId = Integer.parseInt(jwtPrincipal.getMemberId());

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(AccommodationNotFoundException::new);
        List<Room> rooms = roomRepository.findAllByAccommodation(accommodation);

        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            roomResponses.add(RoomResponse.fromEntity(room));
        }

        return roomResponses;
    }
}
