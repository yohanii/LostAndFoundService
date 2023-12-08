package com.yohanii.lostandfound.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public User(String name, String loginId, String password, String nickName, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
}
