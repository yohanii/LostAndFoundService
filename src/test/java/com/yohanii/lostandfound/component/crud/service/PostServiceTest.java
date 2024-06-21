package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Test
    @DisplayName("dto를 받아서, post, item, images을 저장한다.")
    void savePost_정상입력() {
    }

    @Test
    @DisplayName("image가 없다면, post, item만 저장한다.")
    void savePost_no_image() {
    }

    @Test
    @DisplayName("memberId에 해당하는 member가 없으면 exception")
    void savePost_no_member() {
        PostSaveInfoDto dto = new PostSaveInfoDto();
        dto.setMemberId(-1L);

        Assertions.assertThatThrownBy(() -> postService.savePost(dto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("")
    void findPostsByType() {

    }
}