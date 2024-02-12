package com.yohanii.lostandfound.service.file;

import com.yohanii.lostandfound.domain.image.Image;
import com.yohanii.lostandfound.domain.image.ImageRepository;
import com.yohanii.lostandfound.domain.image.ImageType;
import com.yohanii.lostandfound.dto.image.ItemImagesSaveDto;
import com.yohanii.lostandfound.dto.image.ProfileImageSaveDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public Image saveImage(ProfileImageSaveDto dto){

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
            return profileImage;
        }

        Image saveImage = Image.builder()
                .member(dto.getMember())
                .type(ImageType.MEMBER)
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .build();

        imageRepository.save(saveImage);

        return saveImage;
    }

    @Transactional
    public List<Image> saveImages(ItemImagesSaveDto dto) {

        if (dto.getImages().isEmpty()) {
            throw new IllegalArgumentException("ProfileImage is Empty");
        }

        List<Image> itemImages = dto.getItem().getImages();
        if (!itemImages.isEmpty()) {
            log.info("deleteAll");
            imageRepository.deleteAll(itemImages);
        }

        List<Image> images = new ArrayList<>();
        for (MultipartFile file : dto.getImages()) {
            String uploadFileName = file.getOriginalFilename();
            String storeFileName = createStoreFileName(uploadFileName);

            try {
                file.transferTo(new File(getFullPath(storeFileName)));
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            Image saveImage = Image.builder()
                    .item(dto.getItem())
                    .type(ImageType.ITEM)
                    .uploadFileName(uploadFileName)
                    .storeFileName(storeFileName)
                    .build();

            imageRepository.save(saveImage);
            images.add(saveImage);
        }

        return images;
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
