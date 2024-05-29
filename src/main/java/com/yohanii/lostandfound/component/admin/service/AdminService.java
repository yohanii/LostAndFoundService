package com.yohanii.lostandfound.component.admin.service;

import com.yohanii.lostandfound.component.admin.dto.OverviewResponseDto;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void deleteMembers(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return;
        }

        memberRepository.deleteAll(memberIds);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void deletePosts(List<Long> postIds) {
        if (postIds.isEmpty()) {
            return;
        }

        postRepository.deleteAll(postIds);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public void deleteRooms(List<Long> roomIds) {
        if (roomIds.isEmpty()) {
            return;
        }

        roomRepository.deleteAll(roomIds);
    }

    public OverviewResponseDto getOverview() {

        long memberCount = memberRepository.count();
        long lostPostCount = postRepository.getLostPostCount();
        long foundPostCount = postRepository.getFoundPostCount();
        long roomCount = roomRepository.getRoomCount();

        return new OverviewResponseDto(
                memberCount,
                lostPostCount + foundPostCount,
                lostPostCount,
                foundPostCount,
                roomCount
        );
    }

    @Transactional
    public void updateMembersAuth(List<Long> memberIds) {
        memberRepository.findAll()
                .stream()
                .filter(member -> memberIds.contains(member.getId()))
                .forEach(Member::changeAuth);
    }
}
