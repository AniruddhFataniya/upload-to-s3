package com.rspl.uploadtos3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class S3Controller {
    @Autowired
    S3Service s3Service;

    S3Client s3Client = S3Client.builder()
            .region(Region.AP_SOUTH_1)
            .build();

    AmazonS3 client = AmazonS3ClientBuilder.standard().build();

    String bucketName = "docs-2806";
    String objectKey = "error.html";

    String objectPath = "C:/Users/Aniruddh.Fataniya/Desktop/error.html";

    @PostMapping("/uploadFile")
    public void uploadFile() {
        s3Service.uploadFile(s3Client,bucketName,objectKey,objectPath);
    }

    @PostMapping("/multipartUpload")
    public void multipartUpload() throws IOException, InterruptedException {
        s3Service.multipartUpload(client,bucketName,objectKey,objectPath);
    }
}
