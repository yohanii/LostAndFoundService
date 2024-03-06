package com.yohanii.lostandfound.service.chat;

import com.yohanii.lostandfound.domain.chatting.*;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.dto.chatting.ChattingMessageDto;
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
}