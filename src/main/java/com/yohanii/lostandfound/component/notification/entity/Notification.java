package com.yohanii.lostandfound.component.notification.entity;

import com.yohanii.lostandfound.component.crud.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member receiver;

    @Column(name = "notification_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private LocalDateTime createdTime;

    @Builder
    public Notification(Long id, Member receiver, String content, NotificationType notificationType) {
        this.id = id;
        this.receiver = receiver;
        this.content = content;
        this.notificationType = notificationType;
        this.createdTime = LocalDateTime.now();
    }
}
