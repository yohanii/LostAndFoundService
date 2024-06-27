package com.yohanii.lostandfound.component.notification.service;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.notification.entity.Notification;
import com.yohanii.lostandfound.component.notification.repository.EmitterRepository;
import com.yohanii.lostandfound.component.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;
    @Mock
    EmitterRepository emitterRepository;
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("구독 시, emitter를 생성하고, event 하나를 보낸다.")
    void subscribe() throws IOException {

        //given
        Member member = Member.builder()
                .id(1L)
                .build();
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(emitterRepository.get(member.getId())).willReturn(new SseEmitter());

        //when
        notificationService.subscribe(member.getId());

        //then
        then(emitterRepository).should().deleteById(member.getId());
        then(emitterRepository).should().save(eq(member.getId()), any(SseEmitter.class));

    }

    @Test
    @DisplayName("event 하나를 보내고, 알림을 저장한다")
    void testNotify_정상과정() {

        //given
        Member member = Member.builder()
                .id(1L)
                .build();
        String testData = "test data";

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(emitterRepository.get(member.getId())).willReturn(new SseEmitter());

        //when
        notificationService.notify(member.getId(), testData);

        //then
        then(notificationRepository).should().save(any(Notification.class));

    }

    @Test
    @DisplayName("해당하는 emitter가 존재하지 않아도, 알림은 저장된다.")
    void testNotify_emitter_null일때도_notification_저장() {

        //given
        Member member = Member.builder()
                .id(1L)
                .build();
        String testData = "test data";

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(emitterRepository.get(member.getId())).willReturn(null);

        //when
        notificationService.notify(member.getId(), testData);

        //then
        then(notificationRepository).should().save(any(Notification.class));

    }
}