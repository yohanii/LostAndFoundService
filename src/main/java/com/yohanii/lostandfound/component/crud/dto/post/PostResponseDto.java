package com.yohanii.lostandfound.component.crud.dto.post;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String memberNickName;
    private String itemName;
    private List<String> roomMemberNickName;
    private String title;
    private String content;
    private PostType type;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public PostResponseDto(Post p) {

        this.id = p.getId();
        this.memberNickName = p.getMember().getNickName();
        this.itemName = p.getItem().getName();
        this.roomMemberNickName = p.getRooms().stream()
                .map(r -> r.getMember().getNickName())
                .toList();
        this.title = p.getTitle();
        this.content = p.getContent();
        this.type = p.getType();
        this.createdTime = p.getCreatedTime();
        this.updatedTime = p.getUpdatedTime();
    }
}
