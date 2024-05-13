package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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