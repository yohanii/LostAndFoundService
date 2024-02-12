package com.yohanii.lostandfound;

import com.yohanii.lostandfound.domain.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initComponent {

    private final ImageRepository imageRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        imageRepository.saveDefaultImage();
    }
}
