package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.dto.profile.ProfileEditRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberTest {

    @Test
    @DisplayName("맴버 업데이트 성공")
    void updateMember() {

        //given
        ProfileEditRequestDto dto = new ProfileEditRequestDto();
        dto.setName("testName");
        dto.setNickName("testNickName");

        Member member = Member.builder()
                .name("name")
                .nickName("nickName")
                .build();

        //when
        member.updateMember(dto);

        //then
        assertThat(member.getName()).isEqualTo(dto.getName());
        assertThat(member.getNickName()).isEqualTo(dto.getNickName());
        assertThat(member.getUpdatedTime()).isNotNull();
    }

    @Test
    @DisplayName("Member auth가 MemberAuth.ADMIN 일 때, MemberAuth.MEMBER로 변해야한다.")
    void changeAuth1() {

        //given
        Member member = Member.builder()
                .auth(MemberAuth.ADMIN)
                .build();

        //when
        member.changeAuth();

        //then
        assertThat(member.getAuth()).isEqualTo(MemberAuth.MEMBER);
    }

    @Test
    @DisplayName("Member auth가 MemberAuth.MEMBER 일 때, MemberAuth.ADMIN로 변해야한다.")
    void changeAuth2() {

        //given
        Member member = Member.builder()
                .auth(MemberAuth.MEMBER)
                .build();

        //when
        member.changeAuth();

        //then
        assertThat(member.getAuth()).isEqualTo(MemberAuth.ADMIN);
    }
}
