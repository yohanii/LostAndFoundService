package com.yohanii.lostandfound.web.profile;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserRepository;
import com.yohanii.lostandfound.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping("/{nickName}")
    public String profile(@Login User loginUser, @PathVariable String nickName, Model model) {
        //nickname -> user
        User findUser = userRepository.findByNickName(nickName).orElseThrow(() -> new IllegalStateException("해당 nickName으로 찾을 수 없습니다."));
        log.info("findUser = " + findUser);
        log.info("findUser.nickName = " + findUser.getNickName());

        model.addAttribute("user", findUser);

        if (loginUser.equals(findUser)) {
            model.addAttribute("isLoginUser", true);
        }
        return "profile/profile";
    }

//    @GetMapping("/edit-form/{nickName}")
//    public String editForm(@PathVariable String nickName, Model model) {
//        model.addAttribute("nickName", nickName);
//        return "";
//    }
}
