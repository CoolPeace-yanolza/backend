package com.coolpeace.domain.data.service;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.entity.Sido;
import com.coolpeace.domain.accommodation.entity.Sigungu;
import com.coolpeace.domain.accommodation.entity.type.AccommodationType;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.accommodation.repository.SidoRepository;
import com.coolpeace.domain.data.dto.request.GenerateAccommodationRequest;
import com.coolpeace.domain.data.util.InfoGenerator;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateDataService {

    private final SidoRepository sidoRepository;
    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;
    public Integer generateAccommodation(GenerateAccommodationRequest req) {

        Member member = memberRepository.findById(req.member())
            .orElseThrow(MemberNotFoundException::new);

        List<Accommodation> accommodations = new ArrayList<>();

        for(int i = 0 ; i < req.count() ; i++){
            Sido sido = sidoRepository.findById(1).orElseThrow();
            Sigungu sigungu = sido.getSigungus().get((int) (Math.random() * 4) + 2);

            AccommodationType type = AccommodationType.values()[(int) (Math.random() * AccommodationType.values().length)];

            Accommodation accommodation = new Accommodation(
                InfoGenerator.generateName(type),
                sido,
                sigungu,
                "테스트 상세 주소 " + new Random().nextInt(1000),
                member
            );

            accommodation.setType(type);


            accommodations.add(accommodation);
        }

        accommodationRepository.saveAll(accommodations);

        return accommodations.size();
    }
}
