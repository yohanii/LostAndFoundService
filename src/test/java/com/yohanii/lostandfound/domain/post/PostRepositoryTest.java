package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void save() {
        User user = User.builder().build();
        Post post = Post.builder()
                .user(user)
                .title("hello")
                .content("hello123")
                .type(PostType.LOST)
                .build();

        Long savedId = postRepository.save(post);

        Post savedPost = postRepository.findById(savedId);
        assertThat(savedPost.getUser()).isEqualTo(user);
        assertThat(savedPost.getTitle()).isEqualTo("hello");
        assertThat(savedPost.getContent()).isEqualTo("hello123");
        assertThat(savedPost.getType()).isEqualTo(PostType.LOST);
    }

    @Test
    void findById() {
        Post post1 = Post.builder()
                .title("hello")
                .content("hello123")
                .type(PostType.LOST)
                .build();
        Post post2 = Post.builder()
                .title("hello2")
                .content("hello1234")
                .type(PostType.FOUND)
                .build();
        Long savedId1 = postRepository.save(post1);
        Long savedId2 = postRepository.save(post2);

        Post savedPost1 = postRepository.findById(savedId1);
        Post savedPost2 = postRepository.findById(savedId2);

        assertThat(savedPost1).isEqualTo(post1);
        assertThat(savedPost2).isEqualTo(post2);
    }

    @Test
    void findAll() {
        Post post1 = Post.builder()
                .title("hello")
                .content("hello123")
                .type(PostType.LOST)
                .build();
        Post post2 = Post.builder()
                .title("hello2")
                .content("hello1234")
                .type(PostType.FOUND)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> result = postRepository.findAll();
        assertThat(result).contains(post1, post2);
    }

    @Test
    void delete() {
        Post post1 = Post.builder()
                .title("hello")
                .content("hello123")
                .type(PostType.LOST)
                .build();
        postRepository.save(post1);
        List<Post> postsBefore = postRepository.findAll();

        postRepository.delete(post1.getId());
        List<Post> postsAfter = postRepository.findAll();

        assertThat(postsAfter.size()).isEqualTo(postsBefore.size() - 1);
        assertThat(postsBefore).contains(post1);
        assertThat(postsAfter).doesNotContain(post1);
    }

}