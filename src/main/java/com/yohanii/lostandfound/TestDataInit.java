package com.yohanii.lostandfound;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostMemoryRepository;
import com.yohanii.lostandfound.domain.post.PostType;
import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserMemoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserMemoryRepository userRepository;
    private final PostMemoryRepository postRepository;

    @PostConstruct
    public void init() {
        User user1 = new User();
        user1.setName("yohan");
        user1.setLoginId("test");
        user1.setPassword("test");

        User user2 = new User();
        user2.setName("john");
        user2.setLoginId("test2");
        user2.setPassword("test2");

        userRepository.save(user1);
        userRepository.save(user2);

        postRepository.save(new Post(user1, "test1", "testtesttest", PostType.FOUND));
        postRepository.save(new Post(user1, "test2", "testtesttesttest", PostType.LOST));
        postRepository.save(new Post(user2, "test3", "testtesttesttesttest", PostType.FOUND));
    }
}
