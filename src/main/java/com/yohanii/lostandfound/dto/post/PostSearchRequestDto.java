package com.yohanii.lostandfound.dto.post;

import com.yohanii.lostandfound.domain.post.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSearchRequestDto {

    private String content;
    private PostType type;
}
