package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public boolean saveAdmin() {

        if (memberRepository.findByLoginId("admin").isPresent()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        Member admin = Member.builder()
                .loginId("admin")
                .password("admin")
                .name("admin")
                .nickName("admin")
                .auth(MemberAuth.ADMIN)
                .createdTime(now)
                .updatedTime(now)
                .build();

        memberRepository.save(admin);

        return true;
    }
}
