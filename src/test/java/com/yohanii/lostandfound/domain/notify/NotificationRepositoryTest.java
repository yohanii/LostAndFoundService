package com.yohanii.lostandfound.domain.notify;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findAll() {
        Member testMember1 = Member.builder().build();
        Member testMember2 = Member.builder().build();
        Long memberId1 = memberRepository.save(testMember1);
        Long memberId2 = memberRepository.save(testMember2);

        Notification saveNotification1 = Notification.builder()
                .receiver(testMember1)
                .notificationType(NotificationType.CHATTING)
                .build();
        Notification saveNotification2 = Notification.builder()
                .receiver(testMember1)
                .notificationType(NotificationType.CHATTING)
                .build();
        Notification saveNotification3 = Notification.builder()
                .receiver(testMember2)
                .notificationType(NotificationType.CHATTING)
                .build();

        notificationRepository.save(saveNotification1);
        notificationRepository.save(saveNotification2);
        notificationRepository.save(saveNotification3);

        List<Notification> notifications = notificationRepository.findAll(memberId1);
        assertThat(notifications.size()).isEqualTo(2);
        assertThat(notifications).contains(saveNotification1, saveNotification2);
        assertThat(notifications).doesNotContain(saveNotification3);
    }
}