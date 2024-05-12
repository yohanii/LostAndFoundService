package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    @GetMapping("/")
    public String home(Model model) {

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "home";
        }

        Member findMember = memberRepository.find(loginMember.getId());
        model.addAttribute("member", findMember);

        return "loginHome";
    }
}
