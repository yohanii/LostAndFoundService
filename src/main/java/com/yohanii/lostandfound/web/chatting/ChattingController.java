package com.yohanii.lostandfound.web.chatting;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.service.chat.RoomService;
import com.yohanii.lostandfound.service.notify.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final RoomService roomService;
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;
    private final NotificationService notificationService;

    @GetMapping("/chat/{postId}")
    public String chat(@PathVariable Long postId, Model model) {
        log.info("ChattingController.chat");

        Member loginMember = (Member) model.getAttribute("member");
        Long loginMemberId = loginMember.getId();
        Post post = postRepository.findById(postId);

        Optional<Room> findRoomByIds = roomService.findRoomByIds(loginMemberId, post.getMember().getId());
        if (findRoomByIds.isPresent()) {
            return "redirect:/chat/room/" + findRoomByIds.get().getStoreRoomName();
        }

        RoomSaveRequestDto dto = new RoomSaveRequestDto();
        dto.setMemberId(loginMemberId);
        dto.setPostId(postId);
        dto.setPartnerId(post.getMember().getId());

        Long savedRoomId = roomService.createRoom(dto);

        String storeRoomName = roomService.getStoreRoomNameById(savedRoomId);
        notificationService.notify(post.getMember().getId(), loginMember.getNickName() + "님이 채팅을 새로 걸었습니다.");

        return "redirect:/chat/room/" + storeRoomName;
    }

    @GetMapping("/chat/room/{storeRoomName}")
    public String chattingRoom(@PathVariable String storeRoomName, Model model) {
        Room findRoom = roomRepository.findByStoreRoomName(storeRoomName).orElseThrow(() -> new IllegalArgumentException("room is empty"));
        model.addAttribute("room", findRoom);

        return "chat/chattingRoom";
    }

    @GetMapping("/chat/rooms")
    public String chattingRooms(Model model) {
        Member loginMember = (Member) model.getAttribute("member");

        List<Room> findRooms = roomService.findRoomByMemberId(loginMember.getId());
        if (findRooms.isEmpty()) {
            findRooms = new ArrayList<>();
        }
        model.addAttribute("rooms", findRooms);

        return "chat/chattingRooms";
    }
}
