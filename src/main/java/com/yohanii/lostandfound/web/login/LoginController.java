package com.yohanii.lostandfound.web.login;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.domain.user.UserMemoryRepository;
import com.yohanii.lostandfound.service.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        User loginUser = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);

        return "redirect:/";
    }
}
