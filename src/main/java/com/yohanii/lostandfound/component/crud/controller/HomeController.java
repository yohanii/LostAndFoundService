package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(@ModelAttribute PostSearchRequestDto dto,
                       @PageableDefault(size = 15, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        Page<Post> postPage = postService.findPosts(pageable);
        model.addAttribute("posts", postPage);

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPosts";
        }

        return "posts/posts";
    }
}
