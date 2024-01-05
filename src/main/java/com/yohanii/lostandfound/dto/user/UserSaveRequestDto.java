package com.yohanii.lostandfound.dto.user;

import com.yohanii.lostandfound.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;

    public User toEntity() {
        return User.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .nickName("EMPTY")
                .createdTime(LocalDateTime.now())
                .build();
    }
}
