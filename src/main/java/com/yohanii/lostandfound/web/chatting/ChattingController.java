package com.yohanii.lostandfound.web.chatting;

import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostRepository;
import com.yohanii.lostandfound.dto.chatting.RoomSaveRequestDto;
import com.yohanii.lostandfound.service.chat.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final RoomService roomService;
    private final PostRepository postRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/chat/{postId}")
    public String chat(@PathVariable Long postId, Model model) {
        log.info("ChattingController.chat");

        Room findRoom = postRepository.findById(postId).getRoom();
        if (findRoom != null) {
            return "redirect:/chat/room/" + findRoom.getStoreRoomName();
        }

        RoomSaveRequestDto dto = new RoomSaveRequestDto();
        dto.setMemberId(((Member) model.getAttribute("member")).getId());
        dto.setPostId(postId);
        Post post = postRepository.findById(postId);
        dto.setPartnerId(post.getMember().getId());

        Long savedRoomId = roomService.createRoom(dto);

        String storeRoomName = roomService.getStoreRoomNameById(savedRoomId);

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
        return "chat/chattingRooms";
    }
}
