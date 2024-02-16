package com.yohanii.lostandfound.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yohanii.lostandfound.domain.chatting.ChattingType;
import com.yohanii.lostandfound.domain.chatting.Room;
import com.yohanii.lostandfound.domain.chatting.RoomRepository;
import com.yohanii.lostandfound.dto.chatting.ChattingMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final RoomRepository roomRepository;
    private final ChattingService chattingService;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("handleTextMessage");
        String payload = message.getPayload();
        ChattingMessageDto dto = objectMapper.readValue(payload, ChattingMessageDto.class);
        Room room = roomRepository.find(dto.getRoomId());

        if(!chatRoomSessionMap.containsKey(room.getId())){
            chatRoomSessionMap.put(room.getId(), new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(room.getId());

        removeClosedSession(chatRoomSession);

        if (dto.getType().equals(ChattingType.ENTER)) {
            chatRoomSession.add(session);
            dto.setContent(dto.getMemberId() + "님이 입장했습니다.");
            sendToEachSocket(chatRoomSession, new TextMessage(objectMapper.writeValueAsString(dto)) );
        } else if (dto.getType().equals(ChattingType.QUIT)) {
            chatRoomSession.remove(session);
            dto.setContent(dto.getMemberId() + "님이 퇴장했습니다..");
            sendToEachSocket(chatRoomSession, new TextMessage(objectMapper.writeValueAsString(dto)) );
        } else {
            sendToEachSocket(chatRoomSession, message);
        }

        chattingService.saveChatting(dto);
    }

    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        log.info("sessions= {}", sessions);
        sessions.parallelStream().forEach(roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(session -> !sessions.contains(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);

    }
}
