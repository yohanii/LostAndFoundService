package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
