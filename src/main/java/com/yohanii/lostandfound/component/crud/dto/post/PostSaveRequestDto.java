package com.yohanii.lostandfound.component.crud.dto.post;

import com.yohanii.lostandfound.component.crud.entity.ItemCategory;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

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
    @NotBlank
    private String itemPlace;
    @NotNull
    private ItemCategory itemCategory;
    private List<MultipartFile> itemImages;

    private static ModelMapper modelMapper = new ModelMapper();

    public PostSaveInfoDto toServiceDto(Long memberId) {

        PostSaveInfoDto result = modelMapper.map(this, PostSaveInfoDto.class);
        result.setMemberId(memberId);

        return result;
    }
}
