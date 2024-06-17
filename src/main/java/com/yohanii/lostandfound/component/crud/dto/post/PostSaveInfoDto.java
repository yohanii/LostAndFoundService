package com.yohanii.lostandfound.component.crud.dto.post;

import com.yohanii.lostandfound.component.crud.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.component.crud.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveInfoDto {

    @NotNull
    private Long memberId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private PostType type;
    @NotBlank
    private String itemPlace;
    @NotNull
    private ItemCategory itemCategory;
    private List<MultipartFile> itemImages;

    public Post toPostEntity(Member member) {
        LocalDateTime now = LocalDateTime.now();

        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .createdTime(now)
                .updatedTime(now)
                .build();
    }

    public Item toItemEntity(Post post) {
        return Item.builder()
                .post(post)
                .place(itemPlace)
                .itemCategory(itemCategory)
                .build();
    }

    public ItemImagesSaveDto toItemImagesSaveDto(Item item) {
        return new ItemImagesSaveDto(item, itemImages);
    }
}
