package com.yohanii.lostandfound.component.crud.service;

import com.yohanii.lostandfound.component.chatting.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.chatting.service.RoomService;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void createRoom() {
        Member member = Member.builder().build();
        Long savedMemberId = memberRepository.save(member).getId();
        Post post = Post.builder().build();
        Long savedPostId = postRepository.save(post).getId();

        RoomSaveRequestDto dto = new RoomSaveRequestDto();
        dto.setMemberId(savedMemberId);
        dto.setPartnerId(100L);
        dto.setPostId(savedPostId);
        Long savedRoomId = roomService.createRoom(dto);

        Room savedRoom = roomRepository.findById(savedRoomId).orElse(null);

        assertThat(savedRoom.getMember()).isEqualTo(member);
        assertThat(savedRoom.getPost()).isEqualTo(post);
        assertThat(savedRoom.getPartnerId()).isEqualTo(dto.getPartnerId());
        assertThat(savedRoom.getStoreRoomName()).isNotBlank();
    }

    @Test
    void getStoreRoomNameById() {
        Room room = Room.builder()
                .storeRoomName("testStoreRoomName")
                .build();
        Long savedRoomId = roomRepository.save(room).getId();

        String storeRoomName = roomService.getStoreRoomNameById(savedRoomId);

        assertThat(storeRoomName).isEqualTo(room.getStoreRoomName());
    }

    @Test
    void findRoomByMemberId() {
        Member member = Member.builder().build();
        Member otherMember1 = Member.builder().build();
        Member otherMember2 = Member.builder().build();
        memberRepository.save(member);
        memberRepository.save(otherMember1);
        memberRepository.save(otherMember2);

        Room saveRoom1 = Room.builder()
                .member(member)
                .partnerId(10001L)
                .build();

        Room saveRoom2 = Room.builder()
                .member(otherMember1)
                .partnerId(member.getId())
                .build();

        Room saveRoom3 = Room.builder()
                .member(otherMember2)
                .partnerId(member.getId())
                .build();
        roomRepository.save(saveRoom1);
        roomRepository.save(saveRoom2);
        roomRepository.save(saveRoom3);

        List<Room> findRooms = roomService.findRoomByMemberId(member.getId());
        assertThat(findRooms.size()).isEqualTo(3);
        assertThat(findRooms).contains(saveRoom1, saveRoom2, saveRoom3);
    }

    @Test
    @DisplayName("memberId, partnerId가 일치하는 room을 반환한다.")
    void findByMemberIdAndPartnerId() {
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);

        Room room = Room.builder()
                .member(savedMember)
                .partnerId(-1L)
                .build();

        Room savedRoom = roomRepository.save(room);

        Room result = roomService.findRoomByIds(savedMember.getId(), room.getPartnerId()).orElse(null);

        assertThat(result).isEqualTo(savedRoom);
    }

    @Test
    @DisplayName("memberId, partnerId 서로 바뀐 상태에서도 해당 room을 반환한다.")
    void findByMemberIdAndPartnerId_reverse() {
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);

        Room room = Room.builder()
                .member(savedMember)
                .partnerId(-1L)
                .build();

        Room savedRoom = roomRepository.save(room);

        Room result = roomService.findRoomByIds(room.getPartnerId(), savedMember.getId()).orElse(null);

        assertThat(result).isEqualTo(savedRoom);
    }
}