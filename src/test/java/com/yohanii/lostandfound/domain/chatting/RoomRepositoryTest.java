package com.yohanii.lostandfound.domain.chatting;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.entity.Post;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        Member member = Member.builder().build();
        Post post = Post.builder().build();

        Room saveRoom = Room.builder()
                .member(member)
                .post(post)
                .partnerId(100L)
                .storeRoomName("storeRoomName")
                .build();
        roomRepository.save(saveRoom);

        Room savedRoom = roomRepository.find(saveRoom.getId());

        assertThat(savedRoom.getMember()).isEqualTo(member);
        assertThat(savedRoom.getPost()).isEqualTo(post);
        assertThat(savedRoom.getPartnerId()).isEqualTo(saveRoom.getPartnerId());
        assertThat(savedRoom.getStoreRoomName()).isEqualTo(saveRoom.getStoreRoomName());
        assertThat(savedRoom.getCreatedTime()).isEqualTo(saveRoom.getCreatedTime());
    }

    @Test
    void save_CreatedTime_UpdatedTime_같음() {
        Room room = Room.builder().build();

        roomRepository.save(room);

        Room findRoom = roomRepository.find(room.getId());
        assertThat(findRoom.getCreatedTime()).isEqualTo(findRoom.getUpdatedTime());
    }

    @Test
    void find() {
        Room saveRoom = Room.builder().build();
        Long savedId = roomRepository.save(saveRoom);

        Room findRoom = roomRepository.find(savedId);

        assertThat(findRoom).isEqualTo(saveRoom);
    }

    @Test
    void findAll() {
        Room saveRoom1 = Room.builder().build();
        Room saveRoom2 = Room.builder().build();
        roomRepository.save(saveRoom1);
        roomRepository.save(saveRoom2);

        List<Room> rooms = roomRepository.findAll();

        assertThat(rooms.size()).isEqualTo(2);
        assertThat(rooms).contains(saveRoom1, saveRoom2);
    }

    @Test
    void findByStoreRoomName() {
        Room saveRoom = Room.builder()
                .storeRoomName("storeRoomName")
                .build();
        roomRepository.save(saveRoom);

        Optional<Room> findRoom = roomRepository.findByStoreRoomName(saveRoom.getStoreRoomName());

        assertThat(findRoom).isPresent();
        assertThat(findRoom.get()).isEqualTo(saveRoom);
    }

    @Test
    void findByMemberId() {
        Member member = Member.builder().build();
        Member edgeMember = Member.builder().build();
        memberRepository.save(member);
        memberRepository.save(edgeMember);

        Room saveRoom1 = Room.builder()
                .member(member)
                .partnerId(10001L)
                .build();

        Room saveRoom2 = Room.builder()
                .member(member)
                .partnerId(10002L)
                .build();

        Room saveRoom3 = Room.builder()
                .member(edgeMember)
                .partnerId(member.getId())
                .build();
        roomRepository.save(saveRoom1);
        roomRepository.save(saveRoom2);
        roomRepository.save(saveRoom3);

        List<Room> findRooms = roomRepository.findByMemberId(member.getId());
        assertThat(findRooms.size()).isEqualTo(2);
        assertThat(findRooms).contains(saveRoom1, saveRoom2);
    }

    @Test
    void findByPartnerId() {
        Member member = Member.builder().build();
        Member otherMember1 = Member.builder().build();
        Member otherMember2 = Member.builder().build();
        memberRepository.save(member);
        memberRepository.save(otherMember1);
        memberRepository.save(otherMember2);

        Room saveRoom1 = Room.builder()
                .member(member)
                .partnerId(10001L)
                .build();

        Room saveRoom2 = Room.builder()
                .member(otherMember1)
                .partnerId(member.getId())
                .build();

        Room saveRoom3 = Room.builder()
                .member(otherMember2)
                .partnerId(member.getId())
                .build();
        roomRepository.save(saveRoom1);
        roomRepository.save(saveRoom2);
        roomRepository.save(saveRoom3);

        List<Room> findRooms = roomRepository.findByPartnerId(member.getId());
        assertThat(findRooms.size()).isEqualTo(2);
        assertThat(findRooms).contains(saveRoom2, saveRoom3);
    }

    @Test
    void findByIds() {
        Member member = Member.builder().build();
        Member otherMember1 = Member.builder().build();
        Member otherMember2 = Member.builder().build();
        memberRepository.save(member);
        memberRepository.save(otherMember1);
        memberRepository.save(otherMember2);

        Room saveRoom1 = Room.builder()
                .member(member)
                .partnerId(10001L)
                .build();

        Room saveRoom2 = Room.builder()
                .member(otherMember1)
                .partnerId(member.getId())
                .build();

        Room saveRoom3 = Room.builder()
                .member(otherMember2)
                .partnerId(member.getId())
                .build();
        roomRepository.save(saveRoom1);
        roomRepository.save(saveRoom2);
        roomRepository.save(saveRoom3);

        Optional<Room> findRoom = roomRepository.findByIds(member.getId(), otherMember1.getId());
        assertThat(findRoom).isPresent();
        assertThat(findRoom.get()).isEqualTo(saveRoom2);
    }

    @Test
    void deleteAll() {
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();
        Room room3 = Room.builder().build();

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        roomRepository.deleteAll(List.of(room1.getId(), room2.getId(), room3.getId()));
        assertThat(roomRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    void getRoomCount() {
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();

        roomRepository.save(room1);
        roomRepository.save(room2);

        long roomCount = roomRepository.getRoomCount();
        assertThat(roomCount).isEqualTo(2);
    }
}