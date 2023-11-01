package com.yohanii.lostandfound.domain.chatting;

import com.yohanii.lostandfound.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Chatting {

    private Long id;
    private User user;
    private User partner;
    private String content;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
