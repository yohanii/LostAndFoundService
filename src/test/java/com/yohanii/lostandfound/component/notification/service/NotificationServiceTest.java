package com.yohanii.lostandfound.component.notification.service;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.chatting.repository.EmitterRepository;
import com.yohanii.lostandfound.component.notification.entity.Notification;
import com.yohanii.lostandfound.component.notification.repository.NotificationRepository;
import com.yohanii.lostandfound.component.notification.entity.NotificationType;
import com.yohanii.lostandfound.component.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;
    @Autowired
    EmitterRepository emitterRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void subscribe() {
        Long memberId = 0L;
        notificationService.subscribe(memberId);

        SseEmitter findEmitter = emitterRepository.get(memberId);

        assertThat(findEmitter).isNotNull();
    }

    @Test
    void testNotify_정상과정() {
        Member testMember = Member.builder().build();
        Long memberId = memberRepository.save(testMember);
        String testData = "test data";
        notificationService.subscribe(memberId);

        notificationService.notify(memberId, testData);

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications.size()).isEqualTo(1);
        assertThat(notifications.get(0).getReceiver()).isEqualTo(testMember);
        assertThat(notifications.get(0).getContent()).isEqualTo(testData);
        assertThat(notifications.get(0).getNotificationType()).isEqualTo(NotificationType.CHATTING);
    }

    @Test
    void testNotify_emiiter_null일때도_notification_저장되는지() {
        Member testMember = Member.builder().build();
        Long memberId = memberRepository.save(testMember);
        String testData = "test data";

        notificationService.notify(memberId, testData);

        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications.size()).isEqualTo(1);
        assertThat(notifications.get(0).getReceiver()).isEqualTo(testMember);
        assertThat(notifications.get(0).getContent()).isEqualTo(testData);
        assertThat(notifications.get(0).getNotificationType()).isEqualTo(NotificationType.CHATTING);
    }

//    @Test
//    void readAllNotifications() {
//        Member testMember = Member.builder().build();
//        Long memberId = memberRepository.save(testMember);
//        String testData = "test data";
//
//        notificationService.notify(memberId, testData);
//        notificationService.notify(memberId, testData);
//        notificationService.readAllNotifications(memberId);
//
//        List<Notification> notifications = notificationRepository.findAll(memberId);
//        assertThat(notifications.size()).isEqualTo(2);
//        assertThat(notifications.get(0).getIsRead()).isTrue();
//        assertThat(notifications.get(1).getIsRead()).isTrue();
//    }
}