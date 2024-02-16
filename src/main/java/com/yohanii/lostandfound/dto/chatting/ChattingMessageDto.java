package com.yohanii.lostandfound.dto.chatting;

import com.yohanii.lostandfound.domain.chatting.Chatting;
import com.yohanii.lostandfound.domain.chatting.ChattingType;
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
