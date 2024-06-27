package com.yohanii.lostandfound.component.chatting.repository;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("입력된 roomIds에 해당하는 room들을 모두 제거한다.")
    void deleteAll() {

        //given
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();
        Room room3 = Room.builder().build();

        Long savedRoomId1 = roomRepository.save(room1).getId();
        Long savedRoomId2 = roomRepository.save(room2).getId();
        Long savedRoomId3 = roomRepository.save(room3).getId();

        //when
        roomRepository.deleteAll(List.of(savedRoomId1, savedRoomId2));

        //then
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).doesNotContain(room1, room2);
        assertThat(rooms).contains(room3);

    }

    @Test
    @DisplayName("입력된 roomId에 해당하는 room가 없을 경우, 존재하는 room만 제거한다.")
    void deleteAll_non_exist_roomId() {

        //given
        Room room1 = Room.builder().build();
        Room room2 = Room.builder().build();
        Room room3 = Room.builder().build();

        Long savedRoomId1 = roomRepository.save(room1).getId();
        Long savedRoomId2 = roomRepository.save(room2).getId();
        Long savedRoomId3 = roomRepository.save(room3).getId();
        roomRepository.deleteById(savedRoomId3);

        //when
        roomRepository.deleteAll(List.of(savedRoomId1, savedRoomId2, savedRoomId3));

        //then
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).doesNotContain(room1, room2, room3);
    }

    @Test
    @DisplayName("memberId, partnerId가 일치하는 room을 반환한다.")
    void findByMemberIdAndPartnerId() {

        //given
        Member member = Member.builder().build();
        Member savedMember = memberRepository.save(member);

        Room room = Room.builder()
                .member(savedMember)
                .partnerId(1L)
                .build();

        Room savedRoom = roomRepository.save(room);

        //when
        Room result = roomRepository.findByMemberIdAndPartnerId(savedMember.getId(), room.getPartnerId()).orElse(null);

        //then
        assertThat(result).isEqualTo(savedRoom);
    }
}