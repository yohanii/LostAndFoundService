package com.yohanii.lostandfound.service.chat;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.dto.chatting.RoomSaveRequestDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
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
        Long savedMemberId = memberRepository.save(member);
        Post post = Post.builder().build();
        Long savedPostId = postRepository.save(post);

        RoomSaveRequestDto dto = new RoomSaveRequestDto();
        dto.setMemberId(savedMemberId);
        dto.setPartnerId(100L);
        dto.setPostId(savedPostId);
        Long savedRoomId = roomService.createRoom(dto);

        Room savedRoom = roomRepository.find(savedRoomId);

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
        Long savedRoomId = roomRepository.save(room);

        String storeRoomName = roomService.getStoreRoomNameById(savedRoomId);

        assertThat(storeRoomName).isEqualTo(room.getStoreRoomName());
    }
}