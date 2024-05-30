package com.yohanii.lostandfound.component.chatting.controller;

import com.yohanii.lostandfound.component.chatting.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.component.chatting.entity.Room;
import com.yohanii.lostandfound.component.chatting.repository.RoomRepository;
import com.yohanii.lostandfound.component.chatting.service.RoomService;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${websocket.url}")
    private String webSocketUrl;

    @GetMapping("/chat/{postId}")
    public String chat(@PathVariable Long postId, Model model) {
        log.info("ChattingController.chat");

        Member loginMember = (Member) model.getAttribute("member");
        Long loginMemberId = loginMember.getId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시물이 존재하지 않습니다."));

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
        model.addAttribute("url", webSocketUrl);

        return "chat/chattingRoom";
    }

    @GetMapping("/chat/rooms")
    public String chattingRooms(Model model) {

        //게시물 사진 / 제목 / 시간 / 마지막 채팅
        //room.post.item.image / room.post.title / room.updatedTime / room.chattings.last

        Member loginMember = (Member) model.getAttribute("member");

        List<Room> findRooms = roomService.findRoomByMemberId(loginMember.getId());
        if (findRooms.isEmpty()) {
            findRooms = new ArrayList<>();
        }
        model.addAttribute("rooms", findRooms);

        return "chat/chattingRooms";
    }
}
