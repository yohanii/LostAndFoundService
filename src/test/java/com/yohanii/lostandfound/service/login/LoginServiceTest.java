package com.yohanii.lostandfound.service.login;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LoginService loginService;

    @Test
    void login_회원_있음() {
        User testUser1 = User.builder()
                .name("yohan")
                .loginId("testId")
                .password("testPassword")
                .build();
        Long userId = userRepository.save(testUser1);
        User saveUser = userRepository.find(userId);

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