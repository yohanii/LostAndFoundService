package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Post {

    private Long id;
    private User user;

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private PostType type;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public Post(User user, String title, String content, PostType type) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
    }

}
