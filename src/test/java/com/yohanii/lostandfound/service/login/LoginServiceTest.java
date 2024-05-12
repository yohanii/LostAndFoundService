package com.yohanii.lostandfound.service.login;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.login.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LoginService loginService;

    @Test
    void login_회원_있음() {
        Member testMember1 = Member.builder()
                .name("yohan")
                .loginId("testId")
                .password("testPassword")
                .build();
        Long memberId = memberRepository.save(testMember1);
        Member saveMember = memberRepository.find(memberId);

        Optional<Member> result = loginService.login(saveMember.getLoginId(), saveMember.getPassword());

        assertThat(result.get().getLoginId()).isEqualTo(saveMember.getLoginId());
        assertThat(result.get().getPassword()).isEqualTo(saveMember.getPassword());
    }

    @Test
    void login_회원_없음() {
        Optional<Member> result = loginService.login("123", "123");

        assertThat(result.isEmpty()).isTrue();
    }
}