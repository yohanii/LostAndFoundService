package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveInfoDto;
import com.yohanii.lostandfound.component.crud.entity.Item;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.ItemRepository;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ImageStoreService imageStoreService;
    private final MemberRepository memberRepository;

    @Transactional
    public Long savePost(PostSaveInfoDto dto) {

        Member findMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 member가 존재하지 않습니다."));
        Post savePost = dto.toPostEntity(findMember);
        Long savedPostId = postRepository.save(savePost).getId();
        Item savedItem = itemRepository.save(dto.toItemEntity(savePost));

        if (!dto.getItemImages().isEmpty() && !dto.getItemImages().get(0).isEmpty()) {
            imageStoreService.saveImages(dto.toItemImagesSaveDto(savedItem));
        }
        return savedPostId;
    }


}
