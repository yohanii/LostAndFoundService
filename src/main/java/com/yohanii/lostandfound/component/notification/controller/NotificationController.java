package com.yohanii.lostandfound.component.notification.controller;

import com.yohanii.lostandfound.component.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long id) {
        return notificationService.subscribe(id);
    }

    @PostMapping("/send-data/{id}")
    public void sendData(@PathVariable Long id) {
        notificationService.notify(id, "data");
    }
}

