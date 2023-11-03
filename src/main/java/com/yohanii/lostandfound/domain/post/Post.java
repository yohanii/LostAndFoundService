package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Post {

    private Long id;
    private User user;
    private String title;
    private String content;
    private Integer type; //0 : 찾은 물건 게시글, 1 : 잃어버린 물건 게시글
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Post(User user, String title, String content, Integer type) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
