package com.yohanii.lostandfound.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
