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

//    private final UserMemoryRepository userRepository;
//    private final PostMemoryRepository postRepository;

    @PostConstruct
    public void init() {
//        User testUser1 = User.builder()
//                .name("yohan")
//                .loginId("test")
//                .password("test")
//                .build();
//        User testUser2 = User.builder()
//                .name("john")
//                .loginId("test2")
//                .password("test2")
//                .build();
//
//        User user1 = userRepository.save(testUser1);
//        User user2 = userRepository.save(testUser2);
//
//        Post post1 = Post.builder()
//                .title("test1")
//                .content("testtesttest")
//                .type(PostType.FOUND)
//                .build();
//        Post post2 = Post.builder()
//                .title("test2")
//                .content("testtesttesttest")
//                .type(PostType.LOST)
//                .build();
//        Post post3 = Post.builder()
//                .title("test3")
//                .content("testtesttesttesttest")
//                .type(PostType.FOUND)
//                .build();
//
//        postRepository.save(post1, user1);
//        postRepository.save(post2, user1);
//        postRepository.save(post3, user2);
    }
}
