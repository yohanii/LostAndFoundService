package com.yohanii.lostandfound.component.init;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class InitServiceTest {

    @InjectMocks
    InitService initService;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("admin member가 db에 정상적으로 저장되고 true를 반환해야한다.")
    void saveAdmin_정상() {

        //given
        given(memberRepository.findByLoginId("admin")).willReturn(Optional.ofNullable(null));

        //when
        boolean result = initService.saveAdmin();

        //then
        assertThat(result).isTrue();
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    @DisplayName("loginId가 admin인 member가 이미 있다면, 저장되지 않고 false를 반환해야한다.")
    void saveAdmin_admin_이미_존재() {

        //given
        given(memberRepository.findByLoginId("admin")).willReturn(Optional.of(Member.builder().build()));

        //when
        boolean result = initService.saveAdmin();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("입력값만큼 member를 생성한다")
    void fillMembers_정상() {

        //given
        int count = 2;
        given(memberRepository.count()).willReturn(0L);
        given(memberRepository.saveAll(anyCollection())).willReturn(List.of(Member.builder().build(), Member.builder().build()));

        //when
        int result = initService.fillMembers(count);

        //then
        assertThat(result).isEqualTo(count);
        then(memberRepository).should().saveAll(anyIterable());

    }

    @Test
    @DisplayName("입력값 이상이 이미 존재하면 0 반환하고 아무것도 저장하지 않는다.")
    void fillMembers_1000명_이미_존재시() {

        //given
        int count = 2;
        given(memberRepository.count()).willReturn((long) count);

        //when
        int result = initService.fillMembers(count);

        //then
        assertThat(result).isEqualTo(0);
        then(memberRepository).should(never()).saveAll(anyIterable());
    }

    @Test
    @DisplayName("")
    void fillPosts() {
    }
}