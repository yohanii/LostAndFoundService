package com.yohanii.lostandfound.component.crud.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yohanii.lostandfound.component.crud.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.component.crud.dto.image.ProfileImageSaveDto;
import com.yohanii.lostandfound.component.crud.entity.Image;
import com.yohanii.lostandfound.component.crud.entity.Item;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.repository.ImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImageStoreServiceTest {

    @InjectMocks
    ImageStoreService imageStoreService;
    @Mock
    ImageRepository imageRepository;
    @Mock
    AmazonS3 s3Client;

    @Test
    @DisplayName("프로필 이미지 저장 시 이미지 빈 객체 예외 처리")
    void saveImage_profileImage_isEmpty() {

        //given
        ProfileImageSaveDto dto = new ProfileImageSaveDto(null, null);

        //when -> then
        assertThatThrownBy(() -> imageStoreService.saveImage(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProfileImage is Empty");
    }

    @Test
    @DisplayName("프로필 이미지 저장 성공")
    void saveImage() throws IOException {

        //given
        Member member = Member.builder().build();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpeg", "", InputStream.nullInputStream());

        ProfileImageSaveDto dto = new ProfileImageSaveDto(member, file);

        Image resultImage = Image.builder().build();
        given(imageRepository.save(any(Image.class))).willReturn(resultImage);

        //when
        Image savedImage = imageStoreService.saveImage(dto);

        //then
        assertThat(resultImage).isEqualTo(savedImage);
        then(s3Client).should().putObject(any(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
        then(imageRepository).should().save(any(Image.class));
    }

    @Test
    @DisplayName("아이템 이미지 저장 성공")
    void saveImages() throws IOException {

        //given
        Item item = Item.builder().build();
        MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpeg", "", InputStream.nullInputStream());
        MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpeg", "", InputStream.nullInputStream());

        ItemImagesSaveDto dto = new ItemImagesSaveDto(item, List.of(file1, file2));

        Image resultImage = Image.builder().build();
        given(imageRepository.save(any(Image.class))).willReturn(resultImage);

        //when
        List<Image> savedImages = imageStoreService.saveImages(dto);

        //then
        assertThat(savedImages).contains(resultImage);
        assertThat(savedImages.size()).isEqualTo(2);
        then(s3Client).should(times(2)).putObject(any(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
        then(imageRepository).should(times(2)).save(any(Image.class));
    }
}