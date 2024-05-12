package com.yohanii.lostandfound.component.login.controller;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.login.dto.LoginForm;
import com.yohanii.lostandfound.component.login.service.LoginService;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import com.yohanii.lostandfound.consts.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final NotificationService notificationService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        Optional<Member> loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember.isEmpty()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember.get());
//        notificationService.subscribe(loginMember.get().getId());

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
