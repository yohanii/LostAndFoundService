package com.yohanii.lostandfound.service.file;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.image.ImageRepository;
import com.yohanii.lostandfound.domain.image.ImageType;
import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import com.yohanii.lostandfound.dto.image.ProfileImageSaveDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class ImageStoreServiceTest {

    @Autowired
    ImageStoreService imageStoreService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ImageRepository imageRepository;

    @Test
    void saveImage_profileImage_isEmpty() {
        ProfileImageSaveDto dto = new ProfileImageSaveDto(null, null);

        assertThatThrownBy(() -> imageStoreService.saveImage(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProfileImage is Empty");
    }

    @Test
    void saveImage() throws IOException {
        Member member = Member.builder().build();
        memberRepository.save(member);
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "", InputStream.nullInputStream());
        ProfileImageSaveDto dto = new ProfileImageSaveDto(member, file);

        Long savedId = imageStoreService.saveImage(dto);

        Image findImage = imageRepository.find(savedId);
        assertThat(findImage.getMember()).isEqualTo(member);
        assertThat(findImage.getItem()).isNull();
        assertThat(findImage.getType()).isEqualTo(ImageType.MEMBER);
        assertThat(findImage.getUploadFileName()).isEqualTo(file.getOriginalFilename());
        assertThat(findImage.getStoreFileName()).isNotBlank();
    }
}