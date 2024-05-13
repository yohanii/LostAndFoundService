package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.dto.profile.ProfileEditRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberTest {

    @Test
    void updateMember() {
        ProfileEditRequestDto dto = new ProfileEditRequestDto();
        dto.setName("testName");
        dto.setNickName("testNickName");

        Member member = Member.builder()
                .name("name")
                .nickName("nickName")
                .build();

        member.updateMember(dto);

        assertThat(member.getName()).isEqualTo(dto.getName());
        assertThat(member.getNickName()).isEqualTo(dto.getNickName());
        assertThat(member.getUpdatedTime()).isNotNull();
    }

    @Test
    @DisplayName("Member auth가 MemberAuth.ADMIN 일 때, MemberAuth.MEMBER로 변해야한다.")
    void changeAuth1() {
        Member member = Member.builder()
                .auth(MemberAuth.ADMIN)
                .build();

        member.changeAuth();

        assertThat(member.getAuth()).isEqualTo(MemberAuth.MEMBER);
    }

    @Test
    @DisplayName("Member auth가 MemberAuth.MEMBER 일 때, MemberAuth.ADMIN로 변해야한다.")
    void changeAuth2() {
        Member member = Member.builder()
                .auth(MemberAuth.MEMBER)
                .build();

        member.changeAuth();

        assertThat(member.getAuth()).isEqualTo(MemberAuth.ADMIN);
    }
}
