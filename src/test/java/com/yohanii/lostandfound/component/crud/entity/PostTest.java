package com.yohanii.lostandfound.component.crud.entity;

import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    @DisplayName("포스트 업데이트 성공")
    void updatePost() {

        //given
        Post post = Post.builder()
                .title("post title")
                .content("post content")
                .type(PostType.LOST)
                .build();

        PostEditRequestDto dto = new PostEditRequestDto();
        dto.setTitle("edit title");
        dto.setContent("edit content");
        dto.setType(PostType.FOUND);

        //when
        post.updatePost(dto);

        //then
        assertThat(post.getTitle()).isEqualTo(dto.getTitle());
        assertThat(post.getContent()).isEqualTo(dto.getContent());
        assertThat(post.getType()).isEqualTo(dto.getType());
    }
}