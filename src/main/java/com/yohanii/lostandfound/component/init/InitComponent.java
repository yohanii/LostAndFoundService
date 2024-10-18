package com.yohanii.lostandfound.component.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitComponent {

    private final InitService initService;

    public static final int INIT_MEMBER_COUNT = 10000;
    public static final int INIT_POST_COUNT = 1000;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        initService.saveAdmin();
        initService.fillMembers(INIT_MEMBER_COUNT);
        initService.fillPosts(INIT_POST_COUNT);
    }
}
