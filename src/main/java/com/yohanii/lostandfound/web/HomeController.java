package com.yohanii.lostandfound.web;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.web.argumentresolver.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@Login User loginUser, Model model) {
        if (loginUser == null) {
            return "home";
        }

        log.info("loginUser={}", loginUser);
        model.addAttribute("user", loginUser);
        return "loginHome";
    }
}
