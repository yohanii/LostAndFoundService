package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryOldTest {

    @Autowired
    PostRepositoryOld postRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        Member member = Member.builder().build();
        Post post = Post.builder()
                .member(member)
                .title("hello")
                .content("hello123")
                .type(PostType.LOST)
                .build();

        Long savedId = postRepository.save(post);

        Post savedPost = postRepository.findById(savedId);
        assertThat(savedPost.getMember()).isEqualTo(member);
        assertThat(savedPost.getTitle()).isEqualTo("hello");
        assertThat(savedPost.getContent()).isEqualTo("hello123");
        assertThat(savedPost.getType()).isEqualTo(PostType.LOST);
    }

    @Test
    void save_CreatedTime_UpdatedTime_같음() {
        Post post = Post.builder().build();

        postRepository.save(post);

        Post findPost = postRepository.findById(post.getId());
        assertThat(findPost.getCreatedTime()).isEqualTo(findPost.getUpdatedTime());
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
    void findAllByMemberId() {
        Member member = Member.builder().build();
        memberRepository.save(member);

        Post post1 = Post.builder()
                .member(member)
                .build();
        Post post2 = Post.builder()
                .member(member)
                .build();
        Post post3 = Post.builder().build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> findPosts = postRepository.findAll(member.getId());

        assertThat(findPosts.size()).isEqualTo(2);
        assertThat(findPosts).contains(post1, post2);
        assertThat(findPosts).doesNotContain(post3);
    }

    @Test
    void deleteAll() {
        Post post1 = Post.builder().build();
        Post post2 = Post.builder().build();
        Post post3 = Post.builder().build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        postRepository.deleteAll(List.of(post1.getId(), post2.getId(), post3.getId()));
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void getLostPostCount() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .build();
        Post post2 = Post.builder()
                .type(PostType.FOUND)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        long lostPostCount = postRepository.getLostPostCount();

        assertThat(lostPostCount).isEqualTo(1);
    }

    @Test
    void getFoundPostCount() {
        Post post1 = Post.builder()
                .type(PostType.LOST)
                .build();
        Post post2 = Post.builder()
                .type(PostType.FOUND)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        long foundPostCount = postRepository.getFoundPostCount();

        assertThat(foundPostCount).isEqualTo(1);
    }
}