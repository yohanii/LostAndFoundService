package com.yohanii.lostandfound.domain.member;

import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
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
}
