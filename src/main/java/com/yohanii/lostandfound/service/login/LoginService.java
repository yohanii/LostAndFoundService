package com.yohanii.lostandfound.service.login;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserMemoryRepository;
import com.yohanii.lostandfound.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

//    private final UserMemoryRepository userRepository;
    private final UserRepository userRepository;

    public Optional<User> login(String loginId, String password) {
//        User findUser = userRepository.findByLoginId(loginId)
//                .filter(user -> user.getPassword().equals(password))
//                .orElse(null);
//        return Optional.ofNullable(findUser);
        return Optional.ofNullable(null);
    }
}
