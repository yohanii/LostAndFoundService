package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(@ModelAttribute PostSearchRequestDto dto, Model model) {

        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPosts";
        }

        Member findMember = memberRepository.find(loginMember.getId());
        model.addAttribute("member", findMember);

        return "posts/posts";
    }
}
