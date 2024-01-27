package com.yohanii.lostandfound.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        User testUser = User.builder()
                .loginId("abc")
                .password("abcd")
                .name("userTest")
                .build();
        Long savedId = userRepository.save(testUser);

        User savedUser = userRepository.find(testUser.getId());

        assertThat(savedUser.getId()).isEqualTo(testUser.getId());
        assertThat(savedUser.getLoginId()).isEqualTo(testUser.getLoginId());
        assertThat(savedUser.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(savedUser.getName()).isEqualTo(testUser.getName());
    }

    @Test
    void find() {
        User testUser = User.builder()
                .loginId("")
                .password("")
                .name("userTest")
                .build();
        Long savedId = userRepository.save(testUser);

        User savedUser = userRepository.find(savedId);

        assertThat(savedUser).isEqualTo(testUser);
    }

    @Test
    void findAll() {
        User user1 = User.builder()
                .loginId("")
                .password("")
                .name("userA")
                .build();
        User user2 = User.builder()
                .loginId("")
                .password("")
                .name("userB")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> findUsers = userRepository.findAll();

        assertThat(findUsers).contains(user1, user2);
        assertThat(findUsers.size()).isEqualTo(2);
    }

    @Test
    void findByName() {
        User testUser = User.builder()
                .loginId("")
                .password("")
                .name("userTest")
                .build();
        userRepository.save(testUser);

        List<User> findUser = userRepository.findByName(testUser.getName());

        assertThat(findUser.size()).isEqualTo(1);
        assertThat(findUser).contains(testUser);
    }

    @Test
    void findByLoginId() {
        User testUser = User.builder()
                .loginId("test123456789")
                .password("")
                .name("userTest")
                .build();
        userRepository.save(testUser);

        User findUser = userRepository.findByLoginId(testUser.getLoginId()).get();

        assertThat(findUser).isEqualTo(testUser);
    }

    @Test
    void findByNickName() {
        User testUser = User.builder()
                .nickName("testNickName")
                .build();

        userRepository.save(testUser);

        User findUser = userRepository.findByNickName(testUser.getNickName()).get();

        assertThat(findUser).isEqualTo(testUser);
    }
}