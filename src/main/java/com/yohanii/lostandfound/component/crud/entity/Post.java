package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Item item;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType type;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public Post(Long id, Member member, Item item, String title, String content, PostType type, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.title = title;
        this.content = content;
        this.type = type;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public void updatePost(PostEditRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.type = dto.getType();
        this.updatedTime = LocalDateTime.now();
    }
}
