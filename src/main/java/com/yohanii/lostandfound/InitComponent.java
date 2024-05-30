package com.yohanii.lostandfound;

import com.yohanii.lostandfound.component.crud.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitComponent {

    private final MemberService memberService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        memberService.saveAdmin();
    }
}
