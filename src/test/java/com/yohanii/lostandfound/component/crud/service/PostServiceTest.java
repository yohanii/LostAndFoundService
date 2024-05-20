package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.component.crud.entity.ItemCategory;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("저장된 모든 post들을 반환한다.")
    void findPosts() {

        Post post1 = Post.builder().build();
        Post post2 = Post.builder().build();
        Post post3 = Post.builder().build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> findPosts = postService.findPosts();

        assertThat(findPosts.size()).isEqualTo(3);
        assertThat(findPosts).contains(post1, post2, post3);
    }

    @Test
    @DisplayName("PostType이 LOST인 post들을 반환한다.")
    void findLostPosts() {

        Post post1 = Post.builder().type(PostType.LOST).build();
        Post post2 = Post.builder().type(PostType.LOST).build();
        Post post3 = Post.builder().type(PostType.FOUND).build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> findPosts = postService.findLostPosts();

        assertThat(findPosts.size()).isEqualTo(2);
        assertThat(findPosts).contains(post1, post2);
    }

    @Test
    @DisplayName("PostType이 FOUND인 post들을 반환한다.")
    void findFoundPosts() {
        Post post1 = Post.builder().type(PostType.LOST).build();
        Post post2 = Post.builder().type(PostType.FOUND).build();
        Post post3 = Post.builder().type(PostType.FOUND).build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> findPosts = postService.findFoundPosts();

        assertThat(findPosts.size()).isEqualTo(2);
        assertThat(findPosts).contains(post2, post3);
    }

    @Test
    @DisplayName("dto와 files를 받아서, post와 item을 저장하고, member와 연결된다.")
    void savePost() {

        Member member = Member.builder().build();
        Long memberId = memberRepository.save(member).getId();

        PostSaveRequestDto dto = new PostSaveRequestDto("testTitle", "testContent", PostType.LOST, memberId, "testItemName", "testItemPlace", ItemCategory.SMART_PHONE);
        MockMultipartFile file1 = new MockMultipartFile("file1", new byte[0]);
        MockMultipartFile file2 = new MockMultipartFile("file2", new byte[0]);

        Long savedPostId = postService.savePost(dto, List.of(file1, file2));

        Post findPost = postRepository.findById(savedPostId).orElse(Post.builder().build());

        assertThat(findPost.getTitle()).isEqualTo(dto.getTitle());
        assertThat(findPost.getContent()).isEqualTo(dto.getContent());
        assertThat(findPost.getType()).isEqualTo(dto.getType());
//        assertThat(findPost.getItem().getName()).isEqualTo(dto.getItemName());
//        assertThat(findPost.getItem().getPlace()).isEqualTo(dto.getItemPlace());
//        assertThat(findPost.getItem().getItemCategory()).isEqualTo(dto.getItemCategory());
        assertThat(findPost.getMember()).isEqualTo(member);

    }
}