package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveInfoDto;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("dto를 받아서, post, item, images을 저장한다.")
    void savePost_정상입력() {
    }

    @Test
    @DisplayName("image가 없다면, post, item만 저장한다.")
    void savePost_no_image() {
    }

    @Test
    @DisplayName("memberId에 해당하는 member가 없으면 exception")
    void savePost_no_member() {

        //given
        PostSaveInfoDto dto = new PostSaveInfoDto();
        dto.setMemberId(1L);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when -> then
        Assertions.assertThatThrownBy(() -> postService.savePost(dto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("")
    void findPostsByType() {

    }
}