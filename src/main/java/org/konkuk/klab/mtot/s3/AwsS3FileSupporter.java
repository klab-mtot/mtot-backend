package org.konkuk.klab.mtot.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3FileSupporter {

    private final AmazonS3Client amazonS3Client;

    @Value("${s3.bucket}")
    private String bucket;

    public List<String> uploadImages(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for(MultipartFile multipartFile: multipartFiles)
            fileNames.add(uploadImage(multipartFile));
        return fileNames;
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException{
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String fileName = "photos/" + UUID.randomUUID() + multipartFile.getOriginalFilename();

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e){
            e.printStackTrace();
            throw new IOException("Failed To upload file");
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}
