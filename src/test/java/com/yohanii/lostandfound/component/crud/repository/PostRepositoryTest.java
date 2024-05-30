package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("postIds에 들어와 있는 post들이 모두 제거된다.")
    void deleteAll_정상입력() {
        Post post1 = Post.builder().build();
        Post post2 = Post.builder().build();
        Post post3 = Post.builder().build();

        Long savedPostId1 = postRepository.save(post1).getId();
        Long savedPostId2 = postRepository.save(post2).getId();
        Long savedPostId3 = postRepository.save(post3).getId();

        postRepository.deleteAll(List.of(savedPostId1, savedPostId2, savedPostId3));

        List<Post> posts = postRepository.findAll();
        assertThat(posts).doesNotContain(post1, post2, post3);
    }

    @Test
    @DisplayName("postIds 중 존재하는 post가 일부일 경우에도 존재하는 post들만 정상적으로 제거된다.")
    void deleteAll_3개중_2개만_존재할경우() {
        Post post1 = Post.builder().build();
        Post post2 = Post.builder().build();
        Post post3 = Post.builder().build();

        Long savedPostId1 = postRepository.save(post1).getId();
        Long savedPostId2 = postRepository.save(post2).getId();
        Long savedPostId3 = postRepository.save(post3).getId();
        postRepository.deleteById(savedPostId3);

        postRepository.deleteAll(List.of(savedPostId1, savedPostId2, savedPostId3));

        List<Post> posts = postRepository.findAll();
        assertThat(posts).doesNotContain(post1, post2, post3);
    }

    @Test
    @DisplayName("dto에 type과 content가 비어있을 경우, 전체 post 반환")
    void findAllByPostSearchRequestDto_empty() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .content("cdefg hijklmnop")
                .build();
        Post post2 = Post.builder()
                .type(PostType.LOST)
                .content("bcdefg hijklmnop")
                .build();
        Post post3 = Post.builder()
                .type(PostType.FOUND)
                .content("abcdefg hijklmnop")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> result = postRepository.findAllByTypeAndContent(null, "");
        assertThat(result).contains(post1, post2, post3);
    }

    @Test
    @DisplayName("dto에 type이 들어올 경우, type에 해당하는 posts 반환")
    void findAllByPostSearchRequestDto_type() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .content("cdefg hijklmnop")
                .build();
        Post post2 = Post.builder()
                .type(PostType.LOST)
                .content("bcdefg hijklmnop")
                .build();
        Post post3 = Post.builder()
                .type(PostType.FOUND)
                .content("abcdefg hijklmnop")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> result = postRepository.findAllByTypeAndContent(PostType.LOST, "");
        assertThat(result).contains(post1, post2);
        assertThat(result).doesNotContain(post3);
    }

    @Test
    @DisplayName("dto에 content가 들어올 경우, content가 포함된 posts 반환")
    void findAllByPostSearchRequestDto_content() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .content("cdefg hijklmnop")
                .build();
        Post post2 = Post.builder()
                .type(PostType.LOST)
                .content("bcdefg hijklmnop")
                .build();
        Post post3 = Post.builder()
                .type(PostType.FOUND)
                .content("abcdefg hijklmnop")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> result = postRepository.findAllByTypeAndContent(null, "bcdefg");
        assertThat(result).contains(post2, post3);
        assertThat(result).doesNotContain(post1);
    }

    @Test
    @DisplayName("dto에 type과 content가 모두 들어올 경우, 해당 type이며, content가 포함된 posts 반환")
    void findAllByPostSearchRequestDto_type_and_content() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .content("cdefg hijklmnop")
                .build();
        Post post2 = Post.builder()
                .type(PostType.LOST)
                .content("bcdefg hijklmnop")
                .build();
        Post post3 = Post.builder()
                .type(PostType.FOUND)
                .content("abcdefg hijklmnop")
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> result = postRepository.findAllByTypeAndContent(PostType.LOST, "bcdefg");
        assertThat(result).contains(post2);
        assertThat(result).doesNotContain(post1, post3);
    }
}