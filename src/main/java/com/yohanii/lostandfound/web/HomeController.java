package com.yohanii.lostandfound.web;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserRepository;
import com.yohanii.lostandfound.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String home(@Login User loginUser, Model model) {

        if (loginUser == null) {
            return "home";
        }

        User findUser = userRepository.find(loginUser.getId());

        log.info("loginUser={}", loginUser);
        model.addAttribute("user", findUser);

        return "loginHome";
    }
}
