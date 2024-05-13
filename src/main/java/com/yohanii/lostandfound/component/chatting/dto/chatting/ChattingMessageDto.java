package com.yohanii.lostandfound.component.chatting.dto.chatting;

import com.yohanii.lostandfound.component.chatting.entity.ChattingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChattingMessageDto {

    private Long memberId;
    private Long roomId;
    private ChattingType type;
    private String content;

}
