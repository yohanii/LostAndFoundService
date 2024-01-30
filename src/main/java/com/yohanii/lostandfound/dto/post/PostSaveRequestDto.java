package com.yohanii.lostandfound.dto.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostType;
import com.yohanii.lostandfound.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .createdTime(LocalDateTime.now())
                .build();
    }
}
