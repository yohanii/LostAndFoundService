package com.yohanii.lostandfound.domain.user;

import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    void updateUser() {
        ProfileEditRequestDto dto = new ProfileEditRequestDto();
        dto.setName("testName");
        dto.setNickName("testNickName");

        User user = User.builder()
                .name("name")
                .nickName("nickName")
                .build();

        user.updateUser(dto);

        assertThat(user.getName()).isEqualTo(dto.getName());
        assertThat(user.getNickName()).isEqualTo(dto.getNickName());
        assertThat(user.getUpdatedTime()).isNotNull();
    }
}
