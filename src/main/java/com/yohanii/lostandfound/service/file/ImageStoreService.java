package com.yohanii.lostandfound.service.file;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.image.ImageRepository;
import com.yohanii.lostandfound.domain.image.ImageType;
import com.yohanii.lostandfound.dto.image.ProfileImageSaveDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class ImageStoreService {

    private final ImageRepository imageRepository;

    @Value("${file.dir}")
    private String fileDir;

    @Transactional
    public Long saveImage(ProfileImageSaveDto dto){

        if (dto.getProfileImage() == null) {
            throw new IllegalArgumentException("ProfileImage is Empty");
        }

        String uploadFileName = dto.getProfileImage().getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);

        try {
            dto.getProfileImage().transferTo(new File(getFullPath(storeFileName)));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        Image profileImage = dto.getMember().getProfileImage();
        if (profileImage != null) {
            profileImage.changeFileName(uploadFileName, storeFileName);
            return profileImage.getId();
        }

        Image saveImage = Image.builder()
                .member(dto.getMember())
                .type(ImageType.MEMBER)
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .build();

        return imageRepository.save(saveImage);
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
