package com.yohanii.lostandfound.dto.user;

import com.yohanii.lostandfound.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Builder
    public UserSaveRequestDto(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .build();
    }
}
