package com.yohanii.lostandfound.component.chatting.entity;

import com.yohanii.lostandfound.component.crud.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Chatting {

    @Id
    @GeneratedValue
    @Column(name = "chatting_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "chatting_type")
    private ChattingType type;

    @Column(name = "chatting_content")
    private String content;

    private LocalDateTime createdTime;

    @Builder
    public Chatting(Long id, Member member, Room room, ChattingType type, String content, LocalDateTime createdTime) {
        this.id = id;
        this.member = member;
        this.room = room;
        this.type = type;
        this.content = content;
        this.createdTime = createdTime;
    }
}
