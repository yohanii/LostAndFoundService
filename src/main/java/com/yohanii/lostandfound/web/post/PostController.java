package com.yohanii.lostandfound.web.post;

import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    //@GetMapping("/{postId}")
    public String post() {
        return "";
    }
}
