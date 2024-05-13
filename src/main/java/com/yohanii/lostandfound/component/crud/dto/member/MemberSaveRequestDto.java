package com.yohanii.lostandfound.component.crud.dto.member;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String nickName;
    public Member toEntity() {
        LocalDateTime now = LocalDateTime.now();

        return Member.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .nickName(nickName)
                .createdTime(now)
                .updatedTime(now)
                .auth(MemberAuth.MEMBER)
                .build();
    }
}
