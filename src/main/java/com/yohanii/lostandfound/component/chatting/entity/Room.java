package com.yohanii.lostandfound.component.chatting.entity;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    private String storeRoomName;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "member_id2")
    private Long partnerId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Chatting> chattings = new ArrayList<>();

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public Room(Long id, Member member, Post post, Long partnerId, String storeRoomName) {
        this.id = id;
        this.member = member;
        this.post = post;
        this.partnerId = partnerId;
        this.storeRoomName = storeRoomName;

        LocalDateTime now = LocalDateTime.now();
        this.createdTime = now;
        this.updatedTime = now;
    }

    public LocalDateTime updateUpdatedTime() {
        return this.updatedTime = LocalDateTime.now();
    }
}
