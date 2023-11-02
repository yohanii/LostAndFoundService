package com.yohanii.lostandfound.domain.user;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
