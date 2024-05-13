package com.yohanii.lostandfound.component.chatting.service;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import com.yohanii.lostandfound.component.chatting.dto.chatting.RoomSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Member findMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 유저가 존재하지 않습니다."));

        Room saveRoom = Room.builder()
                .member(findMember)
                .partnerId(dto.getPartnerId())
                .post(postRepository.findById(dto.getPostId()))
                .storeRoomName(createStoreRoomName())
                .build();
        return roomRepository.save(saveRoom);
    }

    public String getStoreRoomNameById(Long id) {
        return roomRepository.find(id).getStoreRoomName();
    }

    public List<Room> findRoomByMemberId(Long id) {
        List<Room> result = new ArrayList<>();
        result.addAll(roomRepository.findByMemberId(id));
        result.addAll(roomRepository.findByPartnerId(id));

        return result;
    }

    public Optional<Room> findRoomByIds(Long memberId, Long partnerId) {
        return roomRepository.findByIds(memberId, partnerId);
    }
    private String createStoreRoomName() {
        return UUID.randomUUID().toString();
    }
}
