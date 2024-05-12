package com.yohanii.lostandfound.component.crud.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ProfileEditRequestDto {

    private String name;
    private String nickName;
    private MultipartFile profileImage;

}
