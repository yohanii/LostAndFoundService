package com.yohanii.lostandfound.service.chat;

import com.yohanii.lostandfound.domain.chatting.Chatting;
import com.yohanii.lostandfound.domain.chatting.ChattingRepository;
import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.dto.chatting.ChattingMessageDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ChattingRepository chattingRepository;

    @Transactional
    public Long saveChatting(ChattingMessageDto dto) {
        Member member = memberRepository.find(dto.getMemberId());
        Room room = roomRepository.find(dto.getRoomId());

        LocalDateTime now = room.updateUpdatedTime();
        Chatting saveChatting = Chatting.builder()
                .member(member)
                .room(room)
                .type(dto.getType())
                .content(dto.getContent())
                .createdTime(now)
                .build();

        return chattingRepository.save(saveChatting);
    }
}
