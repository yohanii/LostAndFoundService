package com.yohanii.lostandfound.service.chat;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.dto.chatting.RoomSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long createRoom(RoomSaveRequestDto dto) {
        Room saveRoom = Room.builder()
                .member(memberRepository.find(dto.getMemberId()))
                .partnerId(dto.getPartnerId())
                .post(postRepository.findById(dto.getPostId()))
                .storeRoomName(createStoreRoomName())
                .build();
        return roomRepository.save(saveRoom);
    }

    public String getStoreRoomNameById(Long id) {
        return roomRepository.find(id).getStoreRoomName();
    }
    private String createStoreRoomName() {
        return UUID.randomUUID().toString();
    }
}
