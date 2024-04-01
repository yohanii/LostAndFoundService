package com.yohanii.lostandfound.service.admin;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
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
}
