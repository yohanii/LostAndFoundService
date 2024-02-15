package com.yohanii.lostandfound.domain.chatting;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.post.Post;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

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
}