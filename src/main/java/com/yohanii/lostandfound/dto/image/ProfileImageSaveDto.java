package com.yohanii.lostandfound.dto.image;

import com.yohanii.lostandfound.domain.member.Member;
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
