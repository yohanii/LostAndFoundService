package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    Post post1, post2, post3;

    @BeforeEach
    void setUp() {

        post1 = Post.builder()
                .type(PostType.LOST)
                .content("cdefg hijklmnop")
                .build();
        post2 = Post.builder()
                .type(PostType.LOST)
                .content("bcdefg hijklmnop")
                .build();
        post3 = Post.builder()
                .type(PostType.FOUND)
                .content("abcdefg hijklmnop")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    @DisplayName("postIds에 들어와 있는 post들이 모두 제거된다.")
    void deleteAll_정상입력() {

        //given
        Long savedPostId1 = post1.getId();
        Long savedPostId2 = post2.getId();
        Long savedPostId3 = post3.getId();

        //when
        postRepository.deleteAll(List.of(savedPostId1, savedPostId2, savedPostId3));

        //then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).doesNotContain(post1, post2, post3);
    }

    @Test
    @DisplayName("postIds 중 존재하는 post가 일부일 경우에도 존재하는 post들만 정상적으로 제거된다.")
    void deleteAll_3개중_2개만_존재할경우() {

        //given
        Long savedPostId1 = post1.getId();
        Long savedPostId2 = post2.getId();
        Long savedPostId3 = post3.getId();

        postRepository.deleteById(savedPostId3);

        //when
        postRepository.deleteAll(List.of(savedPostId1, savedPostId2, savedPostId3));

        //then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).doesNotContain(post1, post2, post3);
    }

    @Test
    @DisplayName("dto에 type과 content가 비어있을 경우, 전체 post 반환")
    void findAllByPostSearchRequestDto_empty() {

        //given
        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<Post> result = postRepository.findAllByTypeAndContent(null, "", pageRequest);

        //then
        assertThat(result).contains(post1, post2, post3);
    }

    @Test
    @DisplayName("dto에 type이 들어올 경우, type에 해당하는 posts 반환")
    void findAllByPostSearchRequestDto_type() {

        //given
        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<Post> result = postRepository.findAllByTypeAndContent(PostType.LOST, "", pageRequest);

        //then
        assertThat(result).contains(post1, post2);
        assertThat(result).doesNotContain(post3);
    }

    @Test
    @DisplayName("dto에 content가 들어올 경우, content가 포함된 posts 반환")
    void findAllByPostSearchRequestDto_content() {

        //given
        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<Post> result = postRepository.findAllByTypeAndContent(null, "bcdefg", pageRequest);

        //then
        assertThat(result).contains(post2, post3);
        assertThat(result).doesNotContain(post1);
    }

    @Test
    @DisplayName("dto에 type과 content가 모두 들어올 경우, 해당 type이며, content가 포함된 posts 반환")
    void findAllByPostSearchRequestDto_type_and_content() {

        //given
        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<Post> result = postRepository.findAllByTypeAndContent(PostType.LOST, "bcdefg", pageRequest);

        //then
        assertThat(result).contains(post2);
        assertThat(result).doesNotContain(post1, post3);
    }
}