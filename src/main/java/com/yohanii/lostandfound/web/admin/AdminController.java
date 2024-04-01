package com.yohanii.lostandfound.web.admin;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    @GetMapping
    public String adminHome() {
        return "admin/adminHome";
    }

    @GetMapping("/members")
    public String adminMembers(Model model) {

        List<Member> members = adminService.findAllMembers();
        model.addAttribute("members", members);

        return "admin/adminMembers";
    }

    @PostMapping("/members")
    public String deleteMembers(@RequestParam List<Long> memberIds) {

        log.info("memberIds: " + memberIds);
        adminService.deleteMembers(memberIds);

        return "redirect:/admin/members";
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