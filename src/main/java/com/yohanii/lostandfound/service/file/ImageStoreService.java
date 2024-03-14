package com.yohanii.lostandfound.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageStoreService {

    private final ImageRepository imageRepository;

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder}")
    private String folder;

    @Transactional
    public Image saveImage(ProfileImageSaveDto dto){

        if (dto.getProfileImage() == null) {
            throw new IllegalArgumentException("ProfileImage is Empty");
        }

        String uploadFileName = dto.getProfileImage().getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);

        saveFileS3(storeFileName, dto.getProfileImage(), ImageType.MEMBER);

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
            throw new IllegalArgumentException("ItemImage is Empty");
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

            saveFileS3(storeFileName, file, ImageType.ITEM);

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

    private void saveFileS3(String storeFileName, MultipartFile file, ImageType type) {
        final String path = folder + type.getS3Path() + storeFileName;
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (final InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(bucket, path, inputStream, metadata);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
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
