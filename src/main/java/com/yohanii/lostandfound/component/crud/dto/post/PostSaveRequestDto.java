package com.yohanii.lostandfound.component.crud.dto.post;

import com.yohanii.lostandfound.component.crud.entity.ItemCategory;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSaveRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private PostType type;

    private Long memberId;
    private String itemName;
    private String itemPlace;
    private ItemCategory itemCategory;
//    private List<MultipartFile> itemImages;

//    public Post toPostEntity(Member member) {
//        LocalDateTime now = LocalDateTime.now();
//
//        return Post.builder()
//                .member(member)
//                .title(title)
//                .content(content)
//                .type(type)
//                .createdTime(now)
//                .updatedTime(now)
//                .build();
//    }
//
//    public Item toItemEntity(Post post) {
//        return Item.builder()
//                .post(post)
//                .name(itemName)
//                .place(itemPlace)
//                .itemCategory(itemCategory)
//                .build();
//    }
}
