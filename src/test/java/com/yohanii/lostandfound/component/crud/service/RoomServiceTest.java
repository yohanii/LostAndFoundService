package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.chatting.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.chatting.service.RoomService;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomService roomService;
    @Mock
    RoomRepository roomRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("채팅방 생성 성공")
    void createRoom() {

        //given
        Member member = Member.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .build();

        RoomSaveRequestDto dto = new RoomSaveRequestDto(member.getId(), 2L, post.getId());

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        Room roomResult = Room.builder()
                .member(member)
                .post(post)
                .partnerId(dto.getPartnerId())
                .build();
        given(roomRepository.save(any(Room.class))).willReturn(roomResult);

        //when
        Room savedRoom = roomService.createRoom(dto);

        //then
        assertThat(savedRoom).isEqualTo(roomResult);
    }

    @Test
    @DisplayName("memberId에 해당하는 채팅방을 반환한다.")
    void findRoomByMemberId() {

        //given
        Member member = Member.builder().id(1L).build();
        Member partner = Member.builder().id(2L).build();

        Room saveRoom1 = Room.builder()
                .member(member)
                .partnerId(partner.getId())
                .build();
        Room saveRoom2 = Room.builder()
                .member(partner)
                .partnerId(member.getId())
                .build();

        given(roomRepository.findAllByMemberId(member.getId())).willReturn(List.of(saveRoom1));
        given(roomRepository.findAllByPartnerId(member.getId())).willReturn(List.of(saveRoom2));

        //when
        List<Room> findRooms = roomService.findRoomByMemberId(member.getId());

        //then
        assertThat(findRooms.size()).isEqualTo(2);
        assertThat(findRooms).contains(saveRoom1, saveRoom2);
    }

    @Test
    @DisplayName("memberId, partnerId가 일치하는 room을 반환한다.")
    void findByMemberIdAndPartnerId() {

        //given
        Member member = Member.builder().id(1L).build();
        Member partner = Member.builder().id(2L).build();

        Room saveRoom = Room.builder()
                .member(member)
                .partnerId(partner.getId())
                .build();

        given(roomRepository.findByMemberIdAndPartnerId(member.getId(), partner.getId())).willReturn(Optional.of(saveRoom));

        //when
        Room result = roomService.findRoomByIds(member.getId(), partner.getId()).orElse(null);

        //then
        assertThat(result).isEqualTo(saveRoom);
    }

    @Test
    @DisplayName("memberId, partnerId 서로 바뀐 상태에서도 해당 room을 반환한다.")
    void findByMemberIdAndPartnerId_reverse() {

        //given
        Member member = Member.builder().id(1L).build();
        Member partner = Member.builder().id(2L).build();

        Room saveRoom = Room.builder()
                .member(member)
                .partnerId(partner.getId())
                .build();

        given(roomRepository.findByMemberIdAndPartnerId(partner.getId(), member.getId())).willReturn(Optional.of(saveRoom));

        //when
        Room result = roomService.findRoomByIds(partner.getId(), member.getId()).orElse(null);

        //then
        assertThat(result).isEqualTo(saveRoom);
    }
}