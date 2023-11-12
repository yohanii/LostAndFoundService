package com.yohanii.lostandfound.dto.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private PostType type;

    @Builder
    public PostSaveRequestDto(String title, String content, PostType type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .type(type)
                .build();
    }
}
