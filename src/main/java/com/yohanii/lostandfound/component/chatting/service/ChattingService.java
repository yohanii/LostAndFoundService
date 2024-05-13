package com.yohanii.lostandfound.component.chatting.service;

import com.yohanii.lostandfound.component.chatting.entity.Chatting;
import com.yohanii.lostandfound.component.chatting.repository.ChattingRepository;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.chatting.dto.chatting.ChattingMessageDto;
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
