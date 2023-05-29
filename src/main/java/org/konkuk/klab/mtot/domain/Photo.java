package org.konkuk.klab.mtot.domain;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
public class Photo {
    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private MultipartFile picture;

    @Column(length = 200)
    private String pictureUrl;

    public void setPicture(byte[] fileBytes) throws IOException {
        String uploadDir = System.getProperty("user.dir") + File.separator + "Photo" + File.separator;
        String fileName = generateUniqueFileName(); // 파일명 (고유한 이름으로 생성)

        String filePath = uploadDir + fileName; // 파일의 전체 경로

        // 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 저장 로직
        Path targetLocation = Path.of(filePath);
        Files.write(targetLocation, fileBytes);

        this.pictureUrl = filePath;
    }

    // 파일명을 고유한 이름으로 생성하는 메서드
    private String generateUniqueFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        String timestamp = dateFormat.format(new Date());
        String extension = getFileExtension();
        return timestamp + "." + extension;
    }

    // 파일의 확장자를 추출하는 메서드
    private String getFileExtension() {
        if (picture != null) {
            String originalFilename = picture.getOriginalFilename();
            int lastIndex = originalFilename.lastIndexOf(".");
            if (lastIndex != -1 && lastIndex < originalFilename.length() - 1) {
                return originalFilename.substring(lastIndex + 1);
            }
        }
        return "";
    }
}
