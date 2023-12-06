package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "user_id")
    private User user;

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
    public Post(User user, String title, String content, PostType type) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
    }
}
