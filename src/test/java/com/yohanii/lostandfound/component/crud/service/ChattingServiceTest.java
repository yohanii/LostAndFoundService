package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.chatting.dto.chatting.ChattingMessageDto;
import com.yohanii.lostandfound.component.chatting.entity.Chatting;
import com.yohanii.lostandfound.component.chatting.entity.ChattingType;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.ChattingRepository;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.chatting.service.ChattingService;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChattingServiceTest {

    @InjectMocks
    ChattingService chattingService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    ChattingRepository chattingRepository;

    @Test
    @DisplayName("실행 시 dto의 memberId, roomId에 해당하는 객체를 담은 Chatting 객체를 반환해야한다.")
    void saveChatting() {

        //given
        Long memberId = 1L;
        Long roomId = 1L;

        Member member = Member.builder().build();
        Room room = Room.builder().build();
        ChattingMessageDto dto = new ChattingMessageDto(memberId, roomId, ChattingType.ENTER, "입장했습니다.");

        given(memberRepository.findById(memberId)).willReturn(Optional.ofNullable(member));
        given(roomRepository.findById(roomId)).willReturn(Optional.ofNullable(room));
        given(chattingRepository.save(any(Chatting.class)))
                .willReturn(Chatting.builder()
                        .member(member)
                        .room(room)
                        .type(dto.getType())
                        .content(dto.getContent())
                        .build());

        //when
        Chatting result = chattingService.saveChatting(dto);

        //then
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getRoom()).isEqualTo(room);
        assertThat(result.getType()).isEqualTo(dto.getType());
        assertThat(result.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    @DisplayName("dto의 memberId에 해당하는 member 없을 시, NoSuchElementException 발생")
    void saveChatting_해당하는_유저_없음() {

        //given
        ChattingMessageDto dto = new ChattingMessageDto(1L, 1L, ChattingType.ENTER, "입장했습니다.");

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> chattingService.saveChatting(dto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("dto의 roomId에 해당하는 room 없을 시, NoSuchElementException 발생")
    void saveChatting_해당하는_채팅방_없음() {

        //given
        ChattingMessageDto dto = new ChattingMessageDto(1L, 1L, ChattingType.ENTER, "입장했습니다.");

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(Member.builder().build()));
        given(roomRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> chattingService.saveChatting(dto))
                .isInstanceOf(NoSuchElementException.class);
    }
}