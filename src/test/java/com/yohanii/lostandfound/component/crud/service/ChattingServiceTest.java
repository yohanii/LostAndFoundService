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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChattingServiceTest {

    @Autowired
    ChattingService chattingService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ChattingRepository chattingRepository;

    @Test
    void saveChatting() {
        Member member = Member.builder().build();
        Long savedMemberId = memberRepository.save(member).getId();
        Room room = Room.builder().build();
        Long savedRoomId = roomRepository.save(room).getId();


        ChattingMessageDto dto = new ChattingMessageDto(savedMemberId, savedRoomId, ChattingType.ENTER, "입장했습니다.");
        Long savedChattingId = chattingService.saveChatting(dto);

        Chatting findChatting = chattingRepository.findById(savedChattingId).orElse(null);

        assertThat(findChatting.getMember()).isEqualTo(member);
        assertThat(findChatting.getRoom()).isEqualTo(room);
        assertThat(findChatting.getType()).isEqualTo(dto.getType());
        assertThat(findChatting.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    void saveChatting시_room의_updatedPost_update() {
        Member member = Member.builder().build();
        Long savedMemberId = memberRepository.save(member).getId();
        Room room = Room.builder().build();
        Long savedRoomId = roomRepository.save(room).getId();
        ChattingMessageDto dto = new ChattingMessageDto(savedMemberId, savedRoomId, ChattingType.ENTER, "입장했습니다.");

        Long savedChattingId = chattingService.saveChatting(dto);

        Chatting findChatting = chattingRepository.findById(savedChattingId).orElse(null);
        Room chattingRoom = findChatting.getRoom();

        assertThat(chattingRoom.getUpdatedTime()).isEqualTo(findChatting.getCreatedTime());
    }
}