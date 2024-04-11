package com.yohanii.lostandfound.web.admin;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.dto.admin.OverviewResponseDto;
import com.yohanii.lostandfound.service.admin.AdminService;
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
