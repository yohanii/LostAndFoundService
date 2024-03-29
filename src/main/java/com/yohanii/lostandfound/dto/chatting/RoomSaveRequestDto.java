package com.yohanii.lostandfound.dto.chatting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomSaveRequestDto {

    private Long memberId;
    private Long partnerId;
    private Long postId;
}
