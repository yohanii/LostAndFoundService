package com.yohanii.lostandfound.dto.member;

import com.yohanii.lostandfound.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberSaveRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickName;

    private MultipartFile profileImage;

    public Member toEntity() {
        LocalDateTime now = LocalDateTime.now();

        return Member.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .createdTime(now)
                .updatedTime(now)
                .build();
    }
}
