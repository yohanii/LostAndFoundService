package com.yohanii.lostandfound.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMemoryRepositoryTest {
        UserMemoryRepository userRepository = new UserMemoryRepository();

        @BeforeEach
        void beforeEach() {
            userRepository.clearStore();
        }

        @Test
        void save() {
            User user1 = new User();
            User saveuser = userRepository.save(user1);
            User result = userRepository.findById(saveuser.getId());

            assertThat(result).isEqualTo(user1);
        }

        @Test
        void findById() {
            User user1 = new User();
            userRepository.save(user1);

            User result = userRepository.findById(user1.getId());

            assertThat(result).isEqualTo(user1);
        }

        @Test
        void findAll() {
            User user1 = new User();
            User user2 = new User();
            userRepository.save(user1);
            userRepository.save(user2);

            List<User> result = userRepository.findAll();

            assertThat(result.size()).isEqualTo(2);
            assertThat(result).contains(user1, user2);
        }

        @Test
        void clearStore() {
            User user1 = new User();
            userRepository.save(user1);

            userRepository.clearStore();
            List<User> storeList = userRepository.findAll();

            assertThat(storeList).isEmpty();
        }
}
