package com.yohanii.lostandfound.component.init;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;

    @Transactional
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
