package com.yohanii.lostandfound.service.login;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserMemoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private final UserMemoryRepository userRepository = new UserMemoryRepository();
    private final LoginService loginService = new LoginService(userRepository);

    @Test
    void login_회원_있음() {
        User testUser1 = User.builder()
                .name("yohan")
                .loginId("testId")
                .password("testPassword")
                .build();
        User saveUser = userRepository.save(testUser1);

        Optional<User> result = loginService.login(saveUser.getLoginId(), saveUser.getPassword());

        assertThat(result.get().getLoginId()).isEqualTo(saveUser.getLoginId());
        assertThat(result.get().getPassword()).isEqualTo(saveUser.getPassword());
    }

    @Test
    void login_회원_없음() {
        Optional<User> result = loginService.login("123", "123");

        assertThat(result.isEmpty()).isTrue();
    }
}