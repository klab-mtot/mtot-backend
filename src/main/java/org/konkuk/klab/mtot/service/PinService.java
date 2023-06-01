package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Location;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.domain.Pin;
import org.konkuk.klab.mtot.dto.response.PinUpdateResponse;
import org.konkuk.klab.mtot.exception.JourneyNotFoundException;
import org.konkuk.klab.mtot.exception.MemberNotFoundException;
import org.konkuk.klab.mtot.exception.TeamAccessDeniedException;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.konkuk.klab.mtot.repository.PinRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PinService {

    private final PinRepository pinRepository;
    private final JourneyRepository journeyRepository;
    private final MemberRepository memberRepository;

    private final double PIN_DISTINCT_DISTANCE_IN_METER = 100;

    @Transactional
    public PinUpdateResponse pinRequest(String loginEmail, Long journeyId, Location location){

        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(MemberNotFoundException::new);

        Pin beforePin = pinRepository.findFirstPinByMemberIdAndJourneyId(member.getId(), journeyId)
                .orElseGet(() -> createPin(loginEmail, journeyId, location));
        // orElse는 항상 들어가는데, orElseGet은 null일 때에만 들어간다

        if (getDistanceBetweenTwoLocationsInMeter(beforePin.getLocation(), location) > PIN_DISTINCT_DISTANCE_IN_METER){
            Pin newPin = createPin(loginEmail, journeyId, location);
            return new PinUpdateResponse(newPin.getId());
        }
        return new PinUpdateResponse(beforePin.getId());
    }

    private double getDistanceBetweenTwoLocationsInMeter(Location location1, Location location2){
        int earthRadiusKm = 6371;
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c * 1000;
    }

    private Pin createPin(String loginEmail, Long journeyId, Location location){
        Member member = memberRepository.findByEmail(loginEmail)
                .orElseThrow(MemberNotFoundException::new);

        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(JourneyNotFoundException::new);

        journey.getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getEmail().equals(loginEmail))
                .findAny()
                .orElseThrow(TeamAccessDeniedException::new);

        Pin pin = new Pin(member, journey, location);
        return pinRepository.save(pin);
    }

}
