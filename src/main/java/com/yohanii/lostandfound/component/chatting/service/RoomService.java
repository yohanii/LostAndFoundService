package com.yohanii.lostandfound.component.chatting.service;

import com.yohanii.lostandfound.component.chatting.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public Room createRoom(RoomSaveRequestDto dto) {

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시물이 존재하지 않습니다."));

        Room saveRoom = Room.builder()
                .member(member)
                .post(post)
                .partnerId(dto.getPartnerId())
                .storeRoomName(createStoreRoomName())
                .build();

        return roomRepository.save(saveRoom);
    }

    public List<Room> findRoomByMemberId(Long id) {
        List<Room> result = new ArrayList<>();
        result.addAll(roomRepository.findAllByMemberId(id));
        result.addAll(roomRepository.findAllByPartnerId(id));

        return result;
    }

    public Optional<Room> findRoomByIds(Long memberId, Long partnerId) {
        Optional<Room> result = roomRepository.findByMemberIdAndPartnerId(memberId, partnerId);
        if (result.isPresent()) {
            return result;
        }
        return roomRepository.findByMemberIdAndPartnerId(partnerId, memberId);
    }
    private String createStoreRoomName() {
        return UUID.randomUUID().toString();
    }
}
