package com.yohanii.lostandfound.domain.user;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Image profileImage;

    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public User(String name, Image profileImage, String loginId, String password, String nickName, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.name = name;
        this.profileImage = profileImage;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public void updateUser(ProfileEditRequestDto dto) {
        this.name = dto.getName();
        this.nickName = dto.getNickName();
        this.updatedTime = LocalDateTime.now();
    }
}
