package com.yohanii.lostandfound.domain.member;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.notify.Notification;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.dto.profile.ProfileEditRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private String loginId;
    private String password;
    private String nickName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public Member(String name, Image profileImage, String loginId, String password, String nickName, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.name = name;
        this.profileImage = profileImage;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public void updateMember(ProfileEditRequestDto dto) {
        this.name = dto.getName();
        this.nickName = dto.getNickName();
        this.updatedTime = LocalDateTime.now();
    }
}
