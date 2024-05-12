package com.yohanii.lostandfound.component.crud.dto.image;

import com.yohanii.lostandfound.component.crud.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class ProfileImageSaveDto {

    private Member member;
    private MultipartFile profileImage;

}
