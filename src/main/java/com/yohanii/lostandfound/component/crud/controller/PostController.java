package com.yohanii.lostandfound.component.crud.controller;

import com.yohanii.lostandfound.component.crud.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.component.crud.dto.post.*;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.repository.ItemRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepositoryOld;
import com.yohanii.lostandfound.component.crud.service.ImageStoreService;
import com.yohanii.lostandfound.component.crud.service.PostService;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepositoryOld postRepository;
    private final ItemRepository itemRepository;
    private final ImageStoreService imageStoreService;
    private final NotificationService notificationService;

    @GetMapping("/posts")
    public ResponseEntity<PostsResponseDto> posts() {

        List<Post> posts = postService.findPosts();

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(PostResponseDto::new)
                .toList();
        PostsResponseDto postsResponseDto = new PostsResponseDto(postResponseDtos);

        return ResponseEntity.ok(postsResponseDto);
    }

    @GetMapping("/posts/lost")
    public ResponseEntity<PostsResponseDto> postsLost() {

        List<Post> posts = postService.findLostPosts();

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(PostResponseDto::new)
                .toList();
        PostsResponseDto postsResponseDto = new PostsResponseDto(postResponseDtos);

        return ResponseEntity.ok(postsResponseDto);
    }

    @GetMapping("/posts/found")
    public ResponseEntity<PostsResponseDto> postsFound() {

        List<Post> posts = postService.findFoundPosts();

        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(PostResponseDto::new)
                .toList();
        PostsResponseDto postsResponseDto = new PostsResponseDto(postResponseDtos);

        return ResponseEntity.ok(postsResponseDto);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> post(@PathVariable Long postId) {

        Post post = postRepository.findById(postId);
        PostResponseDto postResponseDto = new PostResponseDto(post);

        return ResponseEntity.ok(postResponseDto);
    }

//    @GetMapping("/posts/add-form")
//    public String postSaveForm(@ModelAttribute PostSaveRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
//
//        setDtoPostTypeByRedirectURL(dto, redirectURL);
//        model.addAttribute("redirectURL", redirectURL);
//
//        return "posts/addPostForm";
//    }

    @PostMapping("/posts")
    public ResponseEntity<Long> postSave(@RequestPart PostSaveRequestDto dto, @RequestPart(required = false) List<MultipartFile> files) {

        Long savedPostId = postService.savePost(dto, files);

        return ResponseEntity.ok(savedPostId);
    }

//    @GetMapping("/posts/{postId}/edit-form")
//    public String postEditForm(@PathVariable Long postId, @ModelAttribute PostEditRequestDto dto, @RequestParam(defaultValue = "/") String redirectURL, Model model) {
//
//        Post findPost = postRepository.findById(postId);
//        dto.setTitle(findPost.getTitle());
//        dto.setContent(findPost.getContent());
//        dto.setType(findPost.getType());
//        dto.setItemName(findPost.getItem().getName());
//        dto.setItemPlace(findPost.getItem().getPlace());
//        dto.setItemCategory(findPost.getItem().getItemCategory());
//
//        model.addAttribute("post", findPost);
//        model.addAttribute("redirectURL", redirectURL);
//
//        return "posts/editPostForm";
//    }

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

    private static void setDtoPostTypeByRedirectURL(PostSaveRequestDto dto, String redirectURL) {
        if (redirectURL.contains("lost")) {
            dto.setType(PostType.LOST);
        }
        if (redirectURL.contains("found")) {
            dto.setType(PostType.FOUND);
        }
    }
}
