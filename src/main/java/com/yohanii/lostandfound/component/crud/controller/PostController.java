package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import com.yohanii.lostandfound.component.crud.service.ImageStoreService;
import com.yohanii.lostandfound.component.crud.service.PostService;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final PostService postService;
    private final ImageStoreService imageStoreService;
    private final NotificationService notificationService;

    @GetMapping("/posts/lost")
    public String postsLost(@ModelAttribute PostSearchRequestDto dto,
                            @PageableDefault(size = 15, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
                            Model model,
                            HttpServletRequest request) {

        Page<Post> postPage = postService.findPostsByType(pageable, PostType.LOST);

        model.addAttribute("posts", postPage);
        model.addAttribute("requestURI", request.getRequestURI());

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPostsLost";
        }

        model.addAttribute("notifications", notificationService.findNotificationsById(loginMember.getId()));

        return "posts/postsLost";
    }

    @GetMapping("/posts/found")
    public String postsFound(@ModelAttribute PostSearchRequestDto dto,
                             @PageableDefault(size = 15, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model,
                             HttpServletRequest request) {

        Page<Post> postPage = postService.findPostsByType(pageable, PostType.FOUND);

        model.addAttribute("posts", postPage);
        model.addAttribute("requestURI", request.getRequestURI());

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPostsFound";
        }

        return "posts/postsFound";
    }

    @GetMapping("/posts/{postId}")
    public String post(@PathVariable Long postId, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 존재하지 않습니다."));

        model.addAttribute("post", post);
        model.addAttribute("redirectURL", redirectURL);

        return "posts/post";
    }

    @GetMapping("/posts/add-form")
    public String postSaveForm(@ModelAttribute PostSaveRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        setDtoPostTypeByRedirectURL(dto, redirectURL);
        model.addAttribute("redirectURL", redirectURL);
        
        return "posts/addPostForm";
    }

    @PostMapping("/posts")
    public String postSave(@Validated @ModelAttribute PostSaveRequestDto dto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        if (bindingResult.hasErrors()) {
            return "posts/addPostForm";
        }

        Long loginMemberId = ((Member) model.getAttribute("member")).getId();
        Long savedPostId = postService.savePost(dto.toServiceDto(loginMemberId));

        return "redirect:/posts/" + savedPostId;
    }

    @GetMapping("/posts/{postId}/edit-form")
    public String postEditForm(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 존재하지 않습니다."));
        dto.setTitle(findPost.getTitle());
        dto.setContent(findPost.getContent());
        dto.setType(findPost.getType());
        dto.setItemPlace(findPost.getItem().getPlace());
        dto.setItemCategory(findPost.getItem().getItemCategory());

        model.addAttribute("post", findPost);
        model.addAttribute("redirectURL", redirectURL);

        return "posts/editPostForm";
    }

    @PatchMapping("/posts/{postId}")
    @Transactional
    public String postEdit(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 존재하지 않습니다."));
        findPost.updatePost(dto);
        findPost.getItem().updateItem(dto);

        if (!dto.getItemImages().isEmpty()) {
            if (!dto.getItemImages().get(0).isEmpty()) {
                log.info("postEdit dto.getItemImages().size() = {}", dto.getItemImages().size());
                imageStoreService.saveImages(new ItemImagesSaveDto(findPost.getItem(), dto.getItemImages()));
            }
        }

        return "redirect:/posts/{postId}?redirectURL=" + redirectURL;
    }

    @DeleteMapping("/posts/{postId}")
    @Transactional
    public String postDelete(@PathVariable Long postId, @RequestParam(defaultValue = "/") String redirectURL) {

        postRepository.deleteById(postId);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/posts/search")
    public String postSearch(@ModelAttribute PostSearchRequestDto dto, Model model) {

        log.info("posts search");
        List<Post> searchPosts = postRepository.findAllByTypeAndContent(dto.getType(), dto.getContent());
        model.addAttribute("posts", searchPosts);

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPostsSearch";
        }

        return "posts/postsSearch";
    }

    @GetMapping("/posts/my-posts")
    public String MyPosts(@ModelAttribute PostSearchRequestDto dto, Model model, HttpServletRequest request) {

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "redirect:/";
        }

        List<Post> myPosts = postRepository.findAllByMemberId(loginMember.getId());
        model.addAttribute("posts", myPosts);
        model.addAttribute("requestURI", request.getRequestURI());

        return "posts/myPosts";
    }

    private static void setDtoPostTypeByRedirectURL(PostSaveRequestDto dto, String redirectURL) {
        if (redirectURL.contains("lost")) {
            dto.setType(PostType.LOST);
        }
        if (redirectURL.contains("found")) {
            dto.setType(PostType.FOUND);
        }
    }
}
