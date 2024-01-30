package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.dto.post.PostEditRequestDto;
import com.yohanii.lostandfound.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.web.argumentresolver.Login;
import jakarta.servlet.http.HttpServletRequest;
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

//    @GetMapping("/posts")
    public String posts(Model model) {

        List<Post> postList = postRepository.findAll();

        model.addAttribute("posts", postList);

        return "posts/posts";
    }

    @GetMapping("/posts-lost")
    public String postsLost(@ModelAttribute PostSearchRequestDto dto, Model model, HttpServletRequest request) {

        List<Post> postList = postRepository.findLostPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/postsLost";
    }

    @GetMapping("/posts-found")
    public String postsFound(@ModelAttribute PostSearchRequestDto dto, Model model, HttpServletRequest request) {

        List<Post> postList = postRepository.findFoundPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/postsFound";
    }

    @GetMapping("/posts/{postId}")
    public String post(@PathVariable Long postId, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        Post post = postRepository.findById(postId);

        model.addAttribute("post", post);
        model.addAttribute("redirectURL", redirectURL);

        return "posts/post";
    }

    @GetMapping("/posts/add-form")
    public String postSaveForm(@ModelAttribute PostSaveRequestDto post, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        model.addAttribute("redirectURL", redirectURL);
        
        return "posts/addPostForm";
    }

    @PostMapping("/posts")
    @Transactional
    public String postSave(@Login Member loginMember, @Validated @ModelAttribute PostSaveRequestDto dto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL) {

        if (bindingResult.hasErrors()) {
            return "posts/addPostForm";
        }

        postRepository.save(dto.toEntity(loginMember));

        return "redirect:" + redirectURL;
    }

    @GetMapping("/posts/{postId}/edit-form")
    public String postEditForm(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        Post findPost = postRepository.findById(postId);
        dto.setTitle(findPost.getTitle());
        dto.setContent(findPost.getContent());
        dto.setType(findPost.getType());

        model.addAttribute("post", findPost);
        model.addAttribute("redirectURL", redirectURL);

        return "posts/editPostForm";
    }

    @PatchMapping("/posts/{postId}")
    @Transactional
    public String postEdit(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL) {

        Post findPost = postRepository.findById(postId);
        findPost.updatePost(dto);

        return "redirect:/posts/{postId}?redirectURL=" + redirectURL;
    }

    @DeleteMapping("/posts/{postId}")
    @Transactional
    public String postDelete(@PathVariable Long postId, @RequestParam(defaultValue = "/") String redirectURL) {

        postRepository.delete(postId);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/posts/search")
    public String postSearch(@ModelAttribute PostSearchRequestDto dto, Model model) {

        log.info("posts search");
        List<Post> searchPosts = postRepository.findAll(dto);
        model.addAttribute("posts", searchPosts);

        return "posts/postsSearch";
    }

    @GetMapping("/posts/my-posts")
    public String MyPosts(Model model, HttpServletRequest request) {

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "redirect:/";
        }

        List<Post> myPosts = postRepository.findAll(loginMember.getId());
        model.addAttribute("posts", myPosts);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/posts";
    }
}
