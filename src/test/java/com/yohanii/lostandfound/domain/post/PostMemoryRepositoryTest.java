package com.yohanii.lostandfound.domain.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostMemoryRepositoryTest {

    PostMemoryRepository postRepository = new PostMemoryRepository();

    @AfterEach
    void afterEach() {
        postRepository.clearStore();
    }

    @Test
    void save() {
        Post post1 = new Post();
        Post savePost = postRepository.save(post1);
        Post result = postRepository.findById(savePost.getId());

        assertThat(result).isEqualTo(post1);
    }

    @Test
    void findById() {
        Post post1 = new Post();
        postRepository.save(post1);

        Post result = postRepository.findById(post1.getId());

        assertThat(result).isEqualTo(post1);
    }

    @Test
    void findAll() {
        Post post1 = new Post();
        Post post2 = new Post();
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> result = postRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(post1, post2);
    }

    @Test
    void clearStore() {
        Post post1 = new Post();
        postRepository.save(post1);

        postRepository.clearStore();
        List<Post> storeList = postRepository.findAll();

        assertThat(storeList).isEmpty();
    }
}
