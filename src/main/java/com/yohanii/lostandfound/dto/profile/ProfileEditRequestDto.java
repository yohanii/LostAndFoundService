package com.yohanii.lostandfound.dto.profile;

import com.yohanii.lostandfound.domain.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileEditRequestDto {

    private String name;
    private String nickName;
    private Image profileImage;

}
