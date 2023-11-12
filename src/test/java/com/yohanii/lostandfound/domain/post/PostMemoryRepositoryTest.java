package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostMemoryRepositoryTest {

    private final PostMemoryRepository postRepository = new PostMemoryRepository();
    private Post testPost1, testPost2;
    private User testUser1;

    @BeforeEach
    void beforeEach() {
        testUser1 = User.builder()
                .name("yohan")
                .loginId("testId")
                .password("testPassword")
                .build();
        testPost1 = Post.builder()
                .title("test")
                .content("testtest")
                .type(PostType.FOUND)
                .build();
        testPost2 = Post.builder()
                .title("test2")
                .content("testtest2")
                .type(PostType.LOST)
                .build();
        postRepository.clearStore();
    }

    @Test
    void save() {
        Post result = postRepository.save(testPost1, testUser1);

        assertThat(result.getUser()).isEqualTo(testUser1);
        assertThat(result.getTitle()).isEqualTo(testPost1.getTitle());
        assertThat(result.getContent()).isEqualTo(testPost1.getContent());
        assertThat(result.getType()).isEqualTo(testPost1.getType());
    }

    @Test
    void findById() {
        Post savePost = postRepository.save(testPost1, testUser1);

        Post result = postRepository.findById(savePost.getId());

        assertThat(result).isEqualTo(savePost);
    }

    @Test
    void findAll() {
        Post savePost1 = postRepository.save(testPost1, testUser1);
        Post savePost2 = postRepository.save(testPost2, testUser1);

        List<Post> result = postRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(savePost1, savePost2);
    }

    @Test
    void clearStore() {
        postRepository.save(testPost1, testUser1);

        postRepository.clearStore();

        List<Post> storeList = postRepository.findAll();
        assertThat(storeList).isEmpty();
    }
}
