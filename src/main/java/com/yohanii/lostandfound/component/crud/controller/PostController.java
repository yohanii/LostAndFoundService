package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.repository.ItemRepository;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostEditRequestDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostSaveRequestDto;
import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.service.ImageStoreService;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ImageStoreService imageStoreService;
    private final NotificationService notificationService;

//    @GetMapping("/posts")
    public String posts(Model model) {

        List<Post> postList = postRepository.findAll();

        model.addAttribute("posts", postList);

        return "posts/posts";
    }

    @GetMapping("/posts/lost")
    public String postsLost(@ModelAttribute PostSearchRequestDto dto, Model model, HttpServletRequest request) {

        List<Post> postList = postRepository.findLostPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPostsLost";
        }

        model.addAttribute("notifications", notificationService.findNotificationsById(loginMember.getId()));

        return "posts/postsLost";
    }

    @GetMapping("/posts/found")
    public String postsFound(@ModelAttribute PostSearchRequestDto dto, Model model, HttpServletRequest request) {

        List<Post> postList = postRepository.findFoundPosts();

        model.addAttribute("posts", postList);
        model.addAttribute("requestURI", request.getRequestURI());

        Member loginMember = (Member) model.getAttribute("member");
        if (loginMember == null) {
            return "posts/unLoginPostsFound";
        }

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
    public String postSaveForm(@ModelAttribute PostSaveRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        setDtoPostTypeByRedirectURL(dto, redirectURL);
        model.addAttribute("redirectURL", redirectURL);
        
        return "posts/addPostForm";
    }

    @PostMapping("/posts")
    @Transactional
    public String postSave(@Validated @ModelAttribute PostSaveRequestDto dto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("PostController.postSave bindingResult.hasErrors");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.info(error.getDefaultMessage());
            }
            return "posts/addPostForm";
        }

        Post savePost = dto.toPostEntity((Member) model.getAttribute("member"));
        postRepository.save(savePost);
        Long savedItemId = itemRepository.save(dto.toItemEntity(savePost));

        if (!dto.getItemImages().isEmpty()) {
            imageStoreService.saveImages(new ItemImagesSaveDto(itemRepository.find(savedItemId), dto.getItemImages()));
        }

        return "redirect:" + redirectURL;
    }

    @GetMapping("/posts/{postId}/edit-form")
    public String postEditForm(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {

        Post findPost = postRepository.findById(postId);
        dto.setTitle(findPost.getTitle());
        dto.setContent(findPost.getContent());
        dto.setType(findPost.getType());
        dto.setItemName(findPost.getItem().getName());
        dto.setItemPlace(findPost.getItem().getPlace());
        dto.setItemCategory(findPost.getItem().getItemCategory());

        model.addAttribute("post", findPost);
        model.addAttribute("redirectURL", redirectURL);

        return "posts/editPostForm";
    }

    @PatchMapping("/posts/{postId}")
    @Transactional
    public String postEdit(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL) {

        Post findPost = postRepository.findById(postId);
        findPost.updatePost(dto);
        findPost.getItem().updateItem(dto);

        if (!dto.getItemImages().isEmpty()) {
            imageStoreService.saveImages(new ItemImagesSaveDto(findPost.getItem(), dto.getItemImages()));
        }

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

        List<Post> myPosts = postRepository.findAll(loginMember.getId());
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
