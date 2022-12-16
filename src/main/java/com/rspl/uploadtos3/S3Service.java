package com.rspl.uploadtos3;

import com.amazonaws.services.s3.AmazonS3;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

public interface S3Service {

    public void uploadFile(S3Client client, String bucketName, String objetctKey, String objectPath);

    public void multipartUpload(AmazonS3 client, String bucketName, String objetctKey, String objectPath) throws IOException, InterruptedException;
}
