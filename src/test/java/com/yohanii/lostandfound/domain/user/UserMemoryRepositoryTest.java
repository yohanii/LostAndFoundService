package com.yohanii.lostandfound.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMemoryRepositoryTest {
        private final UserMemoryRepository userRepository = new UserMemoryRepository();
        private User testUser1, testUser2;

        @BeforeEach
        void beforeEach() {
            testUser1 = User.builder()
                    .name("yohan")
                    .loginId("testId")
                    .password("testPassword")
                    .build();
            testUser2 = User.builder()
                    .name("john")
                    .loginId("testId2")
                    .password("testPassword2")
                    .build();
            userRepository.clearStore();
        }

        @Test
        void save() {
            User saveUser = userRepository.save(testUser1);

            assertThat(saveUser.getName()).isEqualTo(testUser1.getName());
            assertThat(saveUser.getLoginId()).isEqualTo(testUser1.getLoginId());
            assertThat(saveUser.getPassword()).isEqualTo(testUser1.getPassword());
        }

        @Test
        void findById() {
            User saveUser = userRepository.save(testUser1);

            User result = userRepository.findById(saveUser.getId());

            assertThat(result).isEqualTo(saveUser);
        }

        @Test
        void findAll() {
            User user1 = userRepository.save(testUser1);
            User user2 = userRepository.save(testUser2);

            List<User> result = userRepository.findAll();

            assertThat(result.size()).isEqualTo(2);
            assertThat(result).contains(user1, user2);
        }

        @Test
        void clearStore() {
            userRepository.save(testUser1);

            userRepository.clearStore();
            List<User> storeList = userRepository.findAll();

            assertThat(storeList).isEmpty();
        }
}
