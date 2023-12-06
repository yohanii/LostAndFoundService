package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;

    @GetMapping
    public String posts(Model model) {
        List<Post> postList = postRepository.findAll();

        model.addAttribute("posts", postList);

        return "posts/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId);

        model.addAttribute("post", post);
        return "posts/post";
    }

    @GetMapping("/add")
    public String postCreate(@ModelAttribute PostSaveRequestDto post) {
        return "posts/addPostForm";
    }

    @PostMapping("/add")
    @Transactional
    public String postSave(@Validated @ModelAttribute PostSaveRequestDto dto, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "posts/addPostForm";
        }

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);
        postRepository.save(dto.toEntity(loginUser));
        return "redirect:/posts";
    }
}
