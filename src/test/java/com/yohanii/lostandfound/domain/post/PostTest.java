package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.dto.post.PostEditRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void updatePost() {
        Post testPost = Post.builder()
                .title("post title")
                .content("post content")
                .type(PostType.LOST)
                .build();

        PostEditRequestDto dto = new PostEditRequestDto();
        dto.setTitle("edit title");
        dto.setContent("edit content");
        dto.setType(PostType.FOUND);

        testPost.updatePost(dto);

        assertThat(testPost.getTitle()).isEqualTo(dto.getTitle());
        assertThat(testPost.getContent()).isEqualTo(dto.getContent());
        assertThat(testPost.getType()).isEqualTo(dto.getType());
    }
}