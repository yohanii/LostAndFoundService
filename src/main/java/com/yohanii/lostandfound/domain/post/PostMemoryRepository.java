package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostMemoryRepository {

    private static Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L;

    public Post save(Post post, User user) {
        Post newPost = Post.builder()
                .id(++sequence)
                .user(user)
                .title(post.getTitle())
                .content(post.getContent())
                .type(post.getType())
                .build();

        store.put(newPost.getId(), newPost);
        return newPost;
    }

    public Post findById(Long id) {
        return store.get(id);
    }

    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
