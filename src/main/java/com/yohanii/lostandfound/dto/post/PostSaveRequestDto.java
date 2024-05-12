package com.yohanii.lostandfound.dto.post;

import com.yohanii.lostandfound.domain.item.Item;
import com.yohanii.lostandfound.domain.item.ItemCategory;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostType;
import com.yohanii.lostandfound.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private PostType type;

    private String itemName;
    private String itemPlace;
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
                .name(itemName)
                .place(itemPlace)
                .itemCategory(itemCategory)
                .build();
    }
}
