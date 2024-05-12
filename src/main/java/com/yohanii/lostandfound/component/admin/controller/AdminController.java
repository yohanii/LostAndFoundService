package com.yohanii.lostandfound.component.admin.controller;

import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.admin.dto.OverviewResponseDto;
import com.yohanii.lostandfound.component.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    @GetMapping
    public String adminHome(Model model) {

        OverviewResponseDto overview = adminService.getOverview();
        model.addAttribute("overview", overview);

        return "admin/adminHome";
    }

    @GetMapping("/members")
    public String adminMembers(Model model) {

        List<Member> members = adminService.findAllMembers();
        model.addAttribute("members", members);

        return "admin/adminMembers";
    }

    @DeleteMapping("/members")
    public String deleteMembers(@RequestParam List<Long> memberIds) {

        log.info("deleteMembers memberIds: " + memberIds);
        adminService.deleteMembers(memberIds);

        return "redirect:/admin/members";
    }

    @GetMapping("/members/auth")
    public String updateAuthForm(Model model) {
        List<Member> members = adminService.findAllMembers();
        model.addAttribute("members", members);

        return "admin/updateAuthForm";
    }

    @GetMapping("/posts")
    public String adminPosts(Model model) {

        List<Post> posts = adminService.findAllPosts();
        model.addAttribute("posts", posts);

        return "admin/adminPosts";
    }

    @DeleteMapping("/posts")
    public String deletePosts(@RequestParam List<Long> postIds) {

        log.info("postIds: " + postIds);
        adminService.deletePosts(postIds);

        return "redirect:/admin/posts";
    }

    @GetMapping("/chattingRooms")
    public String adminChattingRooms(Model model) {

        List<Room> rooms = adminService.findAllRooms();
        model.addAttribute("rooms", rooms);

        return "admin/adminChattingRooms";
    }

    @DeleteMapping("/chattingRooms")
    public String deleteRooms(@RequestParam List<Long> roomIds) {

        log.info("roomIds: " + roomIds);
        adminService.deleteRooms(roomIds);

        return "redirect:/admin/chattingRooms";
    }

    @PatchMapping("/members/auth")
    public String updateAuth(@RequestParam List<Long> memberIds) {
        log.info("updateAuth memberIds: " + memberIds);

        adminService.updateMembersAuth(memberIds);

        return "redirect:/admin/members";
    }

}
