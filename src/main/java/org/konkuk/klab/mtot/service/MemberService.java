package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.dto.request.TeamCreateRequest;
import org.konkuk.klab.mtot.dto.response.MemberGetAllResponse;
import org.konkuk.klab.mtot.dto.response.MemberGetResponse;
import org.konkuk.klab.mtot.dto.response.MemberSignUpResponse;
import org.konkuk.klab.mtot.exception.DuplicateMemberException;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TeamService teamService;

    @Transactional
    public MemberSignUpResponse join(MemberSignUpRequest request){
        validateDuplicateMembers(request);
        Member member = new Member(request.getName(), request.getEmail(), request.getPassword());
        Long memberId = memberRepository.save(member).getId();
        teamService.createTeam(new TeamCreateRequest(memberId, member.getName() + "'s Group"));
        return new MemberSignUpResponse(memberId);
    }

    private void validateDuplicateMembers(MemberSignUpRequest request){
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new DuplicateMemberException();
                });
    }

    public MemberGetAllResponse getAllMembers() {
        List<MemberGetResponse> list = memberRepository.findAll()
                .stream()
                .map(member ->
                    new MemberGetResponse(member.getId(), member.getName(), member.getEmail())
                )
                .collect(Collectors.toList());
        return new MemberGetAllResponse(list);
    }
}
