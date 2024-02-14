package com.yohanii.lostandfound.domain.chatting;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "member_id2")
    private Long partnerId;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Chatting> chattings = new ArrayList<>();

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public Room(Member member, Post post, Long partnerId, String storeRoomName) {
        this.member = member;
        this.post = post;
        this.partnerId = partnerId;
        this.createdTime = LocalDateTime.now();
        this.storeRoomName = storeRoomName;
    }
}
