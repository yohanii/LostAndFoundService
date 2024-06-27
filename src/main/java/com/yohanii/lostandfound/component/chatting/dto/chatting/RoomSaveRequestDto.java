package com.yohanii.lostandfound.component.chatting.dto.chatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomSaveRequestDto {

    private Long memberId;
    private Long partnerId;
    private Long postId;
}
