package com.yohanii.lostandfound.component.notification.service;

import com.yohanii.lostandfound.component.notification.repository.EmitterRepository;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.notification.entity.Notification;
import com.yohanii.lostandfound.component.notification.entity.NotificationType;
import com.yohanii.lostandfound.component.notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(Long memberId) {
        log.info("NotificationService.subscribe");
        SseEmitter emitter = createEmitter(memberId);

        sendToClient(memberId, "EventStream Created. [memberId=" + memberId + "]", NotificationType.GENERAL);
        return emitter;
    }

    public void notify(Long memberId, Object event) {
        log.info("NotificationService.notify");
        sendToClient(memberId, event, NotificationType.CHATTING);
    }

    private void sendToClient(Long id, Object data, NotificationType type) {
        Member receiver = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                if (type.equals(NotificationType.CHATTING)) {
                    emitter.send(SseEmitter.event().id(String.valueOf(id)).name("sseChatting").data(data));
                }
                if (type.equals(NotificationType.GENERAL)) {
                    emitter.send(SseEmitter.event().id(String.valueOf(id)).name("sseGeneral").data(data));
                }
            } catch (IOException exception) {
                emitterRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }

        if (type.equals(NotificationType.CHATTING)) {
            notificationRepository.save(Notification.builder()
                    .receiver(receiver)
                    .content(data.toString())
                    .notificationType(type)
                    .build());
        }
    }

    private SseEmitter createEmitter(Long id) {
        emitterRepository.deleteById(id);

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }

    public List<Notification> findNotificationsById(Long memberId) {
        return notificationRepository.findAllByReceiverId(memberId);
    }
}
