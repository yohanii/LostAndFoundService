package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.dto.post.PostEditRequestDto;
import com.yohanii.lostandfound.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.web.SessionConst;
import com.yohanii.lostandfound.web.argumentresolver.Login;
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
public class PostController {
    private final PostRepository postRepository;

    @GetMapping("/posts")
    public String posts(Model model) {
        List<Post> postList = postRepository.findAll();

        model.addAttribute("posts", postList);

        return "posts/posts";
    }

    @GetMapping("/posts-lost")
    public String postsLost(Model model, HttpServletRequest request) {
        List<Post> postList = postRepository.findLostPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/postsLost";
    }

    @GetMapping("/posts-found")
    public String postsFound(Model model, HttpServletRequest request) {
        List<Post> postList = postRepository.findFoundPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/postsFound";
    }

    @GetMapping("/posts/{postId}")
    public String post(@Login User loginUser, @PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId);

        log.info("loginUser={}", loginUser);
        model.addAttribute("user", loginUser);
        model.addAttribute("post", post);
        return "posts/post";
    }

    @GetMapping("/posts/add-form")
    public String postSaveForm(@ModelAttribute PostSaveRequestDto post, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
        model.addAttribute("redirectURL", redirectURL);
        
        return "posts/addPostForm";
    }

    @PostMapping("/posts")
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

    @GetMapping("/posts/{postId}/edit-form")
    public String postEditForm(@PathVariable("postId") Long postId, @ModelAttribute PostEditRequestDto dto, Model model) {

        Post findPost = postRepository.findById(postId);
        dto.setTitle(findPost.getTitle());
        dto.setContent(findPost.getContent());
        dto.setType(findPost.getType());

        model.addAttribute("post", findPost);

        return "posts/editPostForm";
    }

    @PatchMapping("/posts/{postId}")
    @Transactional
    public String postEdit(@PathVariable("postId") Long postId, @ModelAttribute PostEditRequestDto dto) {

        Post findPost = postRepository.findById(postId);
        findPost.updatePost(dto);

        return "redirect:/posts/{postId}";
    }

    @DeleteMapping("/posts/{postId}")
    @Transactional
    public String postDelete(@PathVariable("postId") Long postId) {

        postRepository.delete(postId);

        return "redirect:/posts";
    }
}
