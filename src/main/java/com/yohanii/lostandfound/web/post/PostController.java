package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostMemoryRepository;
import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    private final PostMemoryRepository postRepository;

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
    public String postCreate(@ModelAttribute Post post) {
        return "posts/addPostForm";
    }

    @PostMapping("/add")
    public String postSave(@Validated @ModelAttribute Post post, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "posts/addPostForm";
        }

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_USER);

        post.setUser(loginUser);
        postRepository.save(post);

        log.info("post={}", post.getTitle(), post.getContent());
        return "redirect:/posts";
    }
}
