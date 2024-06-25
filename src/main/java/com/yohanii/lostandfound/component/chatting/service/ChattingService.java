package com.yohanii.lostandfound.component.chatting.service;

import com.yohanii.lostandfound.component.chatting.dto.chatting.ChattingMessageDto;
import com.yohanii.lostandfound.component.chatting.entity.Chatting;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.ChattingRepository;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
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
    public Chatting saveChatting(ChattingMessageDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 채팅방이 존재하지 않습니다."));

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
