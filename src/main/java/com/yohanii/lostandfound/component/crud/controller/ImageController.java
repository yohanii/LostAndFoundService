package com.yohanii.lostandfound.component.crud.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder}")
    private String folder;

//    @ResponseBody
//    @GetMapping("/{filename}")
//    public Resource downloadImage(@PathVariable String filename) {
//        String type = parse(filename);
//        String path = folder + type + "/" + filename;
//        S3Object s3object = getS3Object(bucket, path);
//
//        return new InputStreamResource(s3object.getObjectContent());
//    }

    @ResponseBody
    @GetMapping("/{filename}")
    public Resource downloadImage(@PathVariable String filename) {
        try {
            // resources/static/img/item/{filename} 경로에서 파일을 가져옵니다.
            log.info("downloadImage filename {}", filename);
            Path file = Paths.get("src/main/resources/static/img/item/" + filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while reading file", e);
        }
    }

    private S3Object getS3Object(String bucket, String path) {
        try {
            return s3Client.getObject(bucket, path);
        } catch (AmazonS3Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException("파일 다운로드에 실패했습니다.");
        }
    }
    private String parse(String filename) {
        return  filename.split("_")[0].toLowerCase();
    }

}
