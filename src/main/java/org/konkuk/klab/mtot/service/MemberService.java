package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Member;
import org.konkuk.klab.mtot.dto.request.MemberSignUpRequest;
import org.konkuk.klab.mtot.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberSignUpRequest request){
        validateDuplicateUsers(request);
        Member member = new Member(request.getName(), request.getEmail(), request.getPassword());
        return memberRepository.save(member).getId();
    }

    private void validateDuplicateUsers(MemberSignUpRequest request){
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(member -> {
                    throw new RuntimeException("이미 존재하는 회원입니다.");
                });
    }

}
