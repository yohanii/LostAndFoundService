package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Item;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.repository.ItemRepository;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public List<Post> findLostPosts() {
        return postRepository.findAllByType(PostType.LOST);
    }

    public List<Post> findFoundPosts() {
        return postRepository.findAllByType(PostType.FOUND);
    }

    public Long savePost(PostSaveRequestDto dto, List<MultipartFile> files) {

        log.info("savePost : files.size = {}", files.size());

        Member findMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();

        Post post = toPostEntity(dto, findMember, now);
        Long savedPostId = postRepository.save(post).getId();

        Item item = toItemEntity(dto, post);
        itemRepository.save(item);

        return savedPostId;
    }

    private static Item toItemEntity(PostSaveRequestDto dto, Post post) {
        return Item.builder()
                .post(post)
                .name(dto.getItemName())
                .place(dto.getItemPlace())
                .itemCategory(dto.getItemCategory())
                .build();
    }

    private static Post toPostEntity(PostSaveRequestDto dto, Member findMember, LocalDateTime now) {
        return Post.builder()
                .member(findMember)
                .title(dto.getTitle())
                .content(dto.getContent())
                .type(dto.getType())
                .createdTime(now)
                .updatedTime(now)
                .build();
    }
}
