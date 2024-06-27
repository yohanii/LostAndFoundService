package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.dto.profile.ProfileEditRequestDto;
import com.yohanii.lostandfound.component.notification.entity.Notification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Image profileImage;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("createdTime desc")
    private List<Notification> notifications;

    private MemberAuth auth;

    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public Member(Long id, String name, Image profileImage, String loginId, String password, String nickName, LocalDateTime createdTime, LocalDateTime updatedTime, MemberAuth auth) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.auth = auth;
    }

    public void updateMember(ProfileEditRequestDto dto) {
        this.name = dto.getName();
        this.nickName = dto.getNickName();
        this.updatedTime = LocalDateTime.now();
    }

    public void changeAuth() {
        if (this.auth.equals(MemberAuth.MEMBER)) {
            this.auth = MemberAuth.ADMIN;
            return;
        }
        this.auth = MemberAuth.MEMBER;
    }
}
