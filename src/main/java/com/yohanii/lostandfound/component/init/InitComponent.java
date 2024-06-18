package com.yohanii.lostandfound.component.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitComponent {

    private final InitService initService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        initService.saveAdmin();
        initService.fillMembers();
        initService.fillPosts();
    }
}
