package com.yohanii.lostandfound.service.chat;

import com.yohanii.lostandfound.component.chatting.service.ChattingService;
import com.yohanii.lostandfound.component.chatting.entity.Chatting;
import com.yohanii.lostandfound.component.chatting.entity.ChattingType;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.chatting.repository.ChattingRepository;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.chatting.dto.chatting.ChattingMessageDto;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

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
        Long savedMemberId = memberRepository.save(member);
        Room room = Room.builder().build();
        Long savedRoomId = roomRepository.save(room);


        ChattingMessageDto dto = new ChattingMessageDto(savedMemberId, savedRoomId, ChattingType.ENTER, "입장했습니다.");
        Long savedChattingId = chattingService.saveChatting(dto);

        Chatting findChatting = chattingRepository.find(savedChattingId);

        assertThat(findChatting.getMember()).isEqualTo(member);
        assertThat(findChatting.getRoom()).isEqualTo(room);
        assertThat(findChatting.getType()).isEqualTo(dto.getType());
        assertThat(findChatting.getContent()).isEqualTo(dto.getContent());
    }

    @Test
    void saveChatting시_room의_updatedPost_update() {
        Member member = Member.builder().build();
        Long savedMemberId = memberRepository.save(member);
        Room room = Room.builder().build();
        Long savedRoomId = roomRepository.save(room);
        ChattingMessageDto dto = new ChattingMessageDto(savedMemberId, savedRoomId, ChattingType.ENTER, "입장했습니다.");

        Long savedChattingId = chattingService.saveChatting(dto);

        Chatting findChatting = chattingRepository.find(savedChattingId);
        Room chattingRoom = findChatting.getRoom();

        assertThat(chattingRoom.getUpdatedTime()).isEqualTo(findChatting.getCreatedTime());
    }
}