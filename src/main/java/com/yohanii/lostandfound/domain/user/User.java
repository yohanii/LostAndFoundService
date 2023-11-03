package com.yohanii.lostandfound.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {

    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
