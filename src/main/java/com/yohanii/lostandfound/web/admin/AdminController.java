package com.yohanii.lostandfound.web.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {


    @GetMapping
    public String adminHome() {
        return "admin/adminHome";
    }

    @GetMapping("/members")
    public String adminMembers() {
        return "admin/adminMembers";
    }

    @GetMapping("/posts")
    public String adminPosts() {
        return "admin/adminPosts";
    }

    @GetMapping("/chattingRooms")
    public String adminChattingRooms() {
        return "admin/adminChattingRooms";
    }
}
