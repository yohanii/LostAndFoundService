package com.yohanii.lostandfound.service.login;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Optional<Member> login(String loginId, String password) {
        Member findMember = memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
        return Optional.ofNullable(findMember);
    }
}
