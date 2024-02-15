package com.yohanii.lostandfound.domain.chatting;

import com.yohanii.lostandfound.domain.member.Member;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ChattingRepositoryTest {

    @Autowired
    ChattingRepository chattingRepository;

    @Test
    void save() {
        Member member = Member.builder().build();
        Room room = Room.builder().build();

        Chatting saveChatting = Chatting.builder()
                .member(member)
                .room(room)
                .type(ChattingType.TALK)
                .content("hi")
                .build();

        Long savedId = chattingRepository.save(saveChatting);

        Chatting savedChatting = chattingRepository.find(savedId);
        assertThat(savedChatting.getMember()).isEqualTo(member);
        assertThat(savedChatting.getRoom()).isEqualTo(room);
        assertThat(savedChatting.getType()).isEqualTo(saveChatting.getType());
        assertThat(savedChatting.getContent()).isEqualTo(saveChatting.getContent());
        assertThat(savedChatting.getCreatedTime()).isNotNull();
    }

    @Test
    void find() {
        Chatting saveChatting = Chatting.builder().build();
        chattingRepository.save(saveChatting);

        Chatting findChatting = chattingRepository.find(saveChatting.getId());

        assertThat(findChatting).isEqualTo(saveChatting);
    }

    @Test
    void findAll() {
        Chatting saveChatting1 = Chatting.builder().build();
        Chatting saveChatting2 = Chatting.builder().build();
        chattingRepository.save(saveChatting1);
        chattingRepository.save(saveChatting2);

        List<Chatting> chattings = chattingRepository.findAll();

        assertThat(chattings.size()).isEqualTo(2);
        assertThat(chattings).contains(saveChatting1, saveChatting2);
    }
}