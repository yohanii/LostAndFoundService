package com.yohanii.lostandfound.dto.post;

import com.yohanii.lostandfound.domain.item.ItemCategory;
import com.yohanii.lostandfound.domain.post.Post;
import com.yohanii.lostandfound.domain.post.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostEditRequestDto {

    private String title;
    private String content;
    private PostType type;

    private String itemName;
    private String itemPlace;
    private ItemCategory itemCategory;
    private List<MultipartFile> itemImages;
}
