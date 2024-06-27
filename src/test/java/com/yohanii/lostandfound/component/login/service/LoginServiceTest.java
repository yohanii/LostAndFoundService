package com.yohanii.lostandfound.component.login.service;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 성공")
    void login_회원_있음() {

        //given
        Member member = Member.builder()
                .id(1L)
                .name("yohan")
                .loginId("testId")
                .password("testPassword")
                .build();

        given(memberRepository.findByLoginId(member.getLoginId())).willReturn(Optional.of(member));

        //when
        Member result = loginService.login(member.getLoginId(), member.getPassword()).orElse(null);

        //then
        assertThat(result).isEqualTo(member);

    }

    @Test
    @DisplayName("해당하는 맴버 없을 시, 로그인 실패")
    void login_회원_없음() {

        //given
        given(memberRepository.findByLoginId(anyString())).willReturn(Optional.empty());

        //when
        Optional<Member> result = loginService.login("testLoginId", "testPassword");

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}