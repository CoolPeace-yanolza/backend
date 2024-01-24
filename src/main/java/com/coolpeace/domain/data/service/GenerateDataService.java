package com.coolpeace.domain.data.service;

import static com.coolpeace.domain.data.util.InfoGenerator.generateAccommodationName;
import static com.coolpeace.domain.data.util.InfoGenerator.generateCouponTitle;
import static com.coolpeace.domain.data.util.InfoGenerator.generateRoomNumber;
import static com.coolpeace.domain.data.util.InfoGenerator.randomNum;

import com.coolpeace.domain.accommodation.entity.Accommodation;
import com.coolpeace.domain.accommodation.entity.Sido;
import com.coolpeace.domain.accommodation.entity.Sigungu;
import com.coolpeace.domain.accommodation.entity.type.AccommodationType;
import com.coolpeace.domain.accommodation.exception.AccommodationNotFoundException;
import com.coolpeace.domain.accommodation.repository.AccommodationRepository;
import com.coolpeace.domain.accommodation.repository.SidoRepository;
import com.coolpeace.domain.coupon.entity.Coupon;
import com.coolpeace.domain.coupon.entity.CouponRooms;
import com.coolpeace.domain.coupon.entity.type.CouponIssuerType;
import com.coolpeace.domain.coupon.entity.type.CouponRoomType;
import com.coolpeace.domain.coupon.entity.type.CouponUseDaysType;
import com.coolpeace.domain.coupon.entity.type.CustomerType;
import com.coolpeace.domain.coupon.entity.type.DiscountType;
import com.coolpeace.domain.coupon.exception.CouponNotFoundException;
import com.coolpeace.domain.coupon.repository.CouponRepository;
import com.coolpeace.domain.data.dto.request.GenerateAccommodationRequest;
import com.coolpeace.domain.data.dto.request.GenerateCouponRequest;
import com.coolpeace.domain.data.dto.request.GenerateReservationRequest;
import com.coolpeace.domain.data.dto.request.GenerateSettlementRequset;
import com.coolpeace.domain.data.dto.request.SettlementStatistic;
import com.coolpeace.domain.data.util.InfoGenerator;
import com.coolpeace.domain.member.entity.Member;
import com.coolpeace.domain.member.exception.MemberNotFoundException;
import com.coolpeace.domain.member.repository.MemberRepository;
import com.coolpeace.domain.reservation.entity.Reservation;
import com.coolpeace.domain.reservation.entity.type.ReservationStatusType;
import com.coolpeace.domain.reservation.repository.ReservationRepository;
import com.coolpeace.domain.room.entity.Room;
import com.coolpeace.domain.room.entity.RoomReservation;
import com.coolpeace.domain.room.repository.RoomRepository;
import com.coolpeace.domain.room.repository.RoomReservationRepository;
import com.coolpeace.domain.settlement.entity.Settlement;
import com.coolpeace.domain.settlement.repository.SettlementRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
    private final CouponRepository couponRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final SettlementRepository settlementRepository;

    public Integer generateAccommodation(GenerateAccommodationRequest req) {

        Member member = memberRepository.findById(req.member())
            .orElseThrow(MemberNotFoundException::new);

        List<Accommodation> accommodations = new ArrayList<>();

        for(int i = 0 ; i < req.count() ; i++){
            Sido sido = sidoRepository.findById(1).orElseThrow();
            Sigungu sigungu = sido.getSigungus().get((int) (Math.random() * 2) + 2);

            AccommodationType type = AccommodationType.values()[(int) (Math.random() * AccommodationType.values().length)];

            Accommodation accommodation = new Accommodation(
                generateAccommodationName(type),
                sido,
                sigungu,
                "테스트 상세 주소 " + new Random().nextInt(1000),
                member
            );

            accommodation.setType(type);



            accommodations.add(accommodation);
        }

        accommodationRepository.saveAll(accommodations);

        for(Accommodation accommodation : accommodations){
            List<Room> rooms = generateRoom(req.room(), accommodation);
        }


        return accommodations.size();
    }

    public Integer generateCoupon(GenerateCouponRequest req) {

        Member member = memberRepository.findById(req.member())
            .orElseThrow(MemberNotFoundException::new);

        List<Coupon> coupons = new ArrayList<>();

        for(int i = 0 ; i < req.count() ; i++){

            DiscountType discountType = Math.random() >= 0.5 ? DiscountType.FIXED_PRICE : DiscountType.FIXED_RATE;
            Integer discountValue = discountType == DiscountType.FIXED_RATE ? randomNum(5, 30) : randomNum(1, 5) * 10000;
            Integer maximunDiscountPrice = discountType == DiscountType.FIXED_RATE ? (randomNum(2, 10) * 10000) : 0;
            CustomerType customerType = CustomerType.values()[randomNum(0, CustomerType.values().length - 1)];
            CouponRoomType couponRoomType = Math.random() > 0.5 ? CouponRoomType.RENTAL : null;
            CouponRoomType couponRoomStayType = Math.random() > 0.6 ? CouponRoomType.LODGE : Math.random() > 0.3 ? CouponRoomType.TWO_NIGHT : null;
            List<DayOfWeek> couponUserConditionDays = getRandomCondiionDays();
            Integer minimumReservationPrice = randomNum(5, 10) * 10000;
            CouponUseDaysType couponUseDays = CouponUseDaysType.values()[randomNum(0, CouponUseDaysType.values().length - 1)];
            LocalDate exposureStartDate = getRandomDateInPeriod();
            LocalDate exposureEndDate = exposureStartDate.plusDays(randomNum(1, 365));
            Integer couponExpiration = randomNum(10, 365);
            Integer downloadCount = randomNum(10, 5000);
            Integer useCount = Math.abs(downloadCount - randomNum(5, downloadCount));
            Accommodation accommodation = accommodationRepository.findById(req.accommodation())
                .orElseThrow(AccommodationNotFoundException::new);


            Coupon coupon = new Coupon(
                generateCouponTitle(),
                discountType,
                discountValue,
                maximunDiscountPrice,
                customerType,
                couponRoomType,
                couponRoomStayType,
                minimumReservationPrice,
                couponUseDays,
                exposureStartDate,
                exposureEndDate,
                accommodation,
                accommodation.getRooms(),
                member
            );

            coupon.setDownloadCount(randomNum(10, 1000));
            coupon.setUseCount(Math.abs(coupon.getDownloadCount() - randomNum(9, 990)));
            coupon.setCouponExpiration(randomNum(10, 31));


            coupons.add(coupon);

        }

        couponRepository.saveAll(coupons);

        for(Coupon coupon : coupons){
            coupon.generateCouponNumber(CouponIssuerType.OWNER, coupon.getId());
            coupon.setCreatedAt(LocalDateTime.of(coupon.getExposureStartDate().minusDays(randomNum(1, 20)), LocalTime.of(0, 0)));
        }
        couponRepository.saveAll(coupons);



        return coupons.size();
    }

    private List<Room> generateRoom(Long roomCount, Accommodation accommodation) {

        List<Room> rooms = new ArrayList<>();

        List<String> namePool = InfoGenerator.getRoomNumberPool();

        for(int i = 0 ; i < roomCount ; i++){

            String roomNumber = generateRoomNumber(namePool);
            String roomType = roomNumber;
            int price = randomNum(5, 30) * 10000;
            
            Room room = new Room(
                roomNumber,
                roomType,
                price,
                accommodation
            );

            rooms.add(room);
            
        }

        roomRepository.saveAll(rooms);

        return rooms;
    }



    private List<DayOfWeek> getRandomCondiionDays(){

        List<DayOfWeek> days = new ArrayList<>(List.of(DayOfWeek.values()));
        Collections.shuffle(days);
        Integer count = randomNum(0, days.size());
        return days.subList(0, count);
    }

    public static LocalDate getRandomDateInPeriod() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        // 특정 기간 내에서 랜덤한 날짜 얻기
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        Random random = new Random();
        long randomDays = random.nextInt((int) daysBetween + 1); // 0부터 daysBetween 사이의 랜덤 값

        return startDate.plusDays(randomDays);
    }


    public Integer generateReservation(GenerateReservationRequest req) {

        List<Reservation> reservations = new ArrayList<>();
        List<RoomReservation> roomReservations = new ArrayList<>();

        for(int i = 0 ; i < req.count() ; i++){

            Room room = roomRepository.findRandom();
            List<CouponRooms> couponRooms = room.getCouponRooms();
            Coupon coupon = couponRooms.get(randomNum(0, couponRooms.size() - 1)).getCoupon();

            LocalDate date = getRandomDateInPeriod();

            Reservation reservation = new Reservation(
                 LocalDateTime.of(date, LocalTime.of(15, 0)),
                LocalDateTime.of(date.plusDays(randomNum(1, 3)), LocalTime.of(11, 0))
            );
            reservation.updateReservation(Math.random() > 0.95 ? ReservationStatusType.CANCELLED : ReservationStatusType.PENDING);

            RoomReservation roomReservation = new RoomReservation(
                room,
                reservation,
                Math.random() > 0.9 ? null : coupon
            );

            reservation.updateRoomReservationAndPrices(
                List.of(roomReservation)
                ,room.getPrice()
            );

            reservations.add(reservation);
            roomReservations.add(roomReservation);

        }

        reservationRepository.saveAll(reservations);
        roomReservationRepository.saveAll(roomReservations);


        return reservations.size();
    }

    public Integer generateSettlemnet(GenerateSettlementRequset req) {

        List<Settlement> settlements = new ArrayList<>();

        List<SettlementStatistic> settlementStatistics = settlementRepository.statisticReservation(req.start(), req.end());

        for(SettlementStatistic ss : settlementStatistics){

            if(ss.getCouponId() == null){
                continue;
            }

            Coupon coupon = couponRepository.findById(ss.getCouponId()).orElseThrow(
                    CouponNotFoundException::new);


            Accommodation accommodation = accommodationRepository.findById(ss.getAccommodationId())
                .orElseThrow(AccommodationNotFoundException::new);

            Settlement settlement = Settlement.builder()
                .coupon(coupon)
                .accommodation(accommodation)
                .couponUseDate(ss.getCouponUseDate())
                .completeAt(ss.getCouponUseDate().plusMonths(1).minusDays(ss.getCouponUseDate().getDayOfMonth()).plusDays(1) )
                .couponCount(ss.getCount())
                .cancelPrice(ss.getCancelPrice() * -1)
                .discountPrice(ss.getDiscountPrice())
                .build();

            settlement.sumPrice();

            settlements.add(settlement);
        }

        settlementRepository.saveAll(settlements);

        return settlements.size();
    }
}
