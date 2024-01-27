package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserRepository;
import com.yohanii.lostandfound.dto.post.PostSearchRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

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
        assertThat(result.size()).isEqualTo(2);
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

    @Test
    void findAllPostSearchRequestDto() {
        Post post1 = Post.builder()
                .content("!@#$%post1")
                .type(PostType.FOUND)
                .build();
        Post post2 = Post.builder()
                .content("!@#$%post2")
                .type(PostType.FOUND)
                .build();
        Post post3 = Post.builder()
                .content("!@#$%post3")
                .type(PostType.LOST)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> searchPosts1 = postRepository.findAll(new PostSearchRequestDto("!@#$", PostType.FOUND));
        List<Post> searchPosts2 = postRepository.findAll(new PostSearchRequestDto("!@#$", PostType.LOST));
        List<Post> searchPosts3 = postRepository.findAll(new PostSearchRequestDto("!@#$", null));

        assertThat(searchPosts1.size()).isEqualTo(2);
        assertThat(searchPosts2.size()).isEqualTo(1);
        assertThat(searchPosts3.size()).isEqualTo(3);
        assertThat(searchPosts1).contains(post1, post2);
        assertThat(searchPosts2).contains(post3);
        assertThat(searchPosts3).contains(post1, post2, post3);
    }

    @Test
    void findAllPostSearchRequestDto_empty_content() {
        Post post1 = Post.builder()
                .content("!@#$%post1")
                .type(PostType.FOUND)
                .build();
        Post post2 = Post.builder()
                .content("!@#$%post2")
                .type(PostType.FOUND)
                .build();
        Post post3 = Post.builder()
                .content("!@#$%post3")
                .type(PostType.LOST)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> searchPosts = postRepository.findAll(new PostSearchRequestDto("", null));

        assertThat(searchPosts.size()).isEqualTo(3);
        assertThat(searchPosts).contains(post1, post2, post3);
    }

    @Test
    void findAllByUserId() {
        User user = User.builder().build();
        userRepository.save(user);

        Post post1 = Post.builder()
                .user(user)
                .build();
        Post post2 = Post.builder()
                .user(user)
                .build();
        Post post3 = Post.builder().build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> findPosts = postRepository.findAll(user.getId());

        assertThat(findPosts.size()).isEqualTo(2);
        assertThat(findPosts).contains(post1, post2);
        assertThat(findPosts).doesNotContain(post3);
    }
}