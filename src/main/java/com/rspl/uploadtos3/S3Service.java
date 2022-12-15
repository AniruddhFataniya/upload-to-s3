package com.rspl.uploadtos3;

import software.amazon.awssdk.services.s3.S3Client;

public interface S3Service {

    public void uploadFile(S3Client client, String bucketName, String objetctKey, String objectPath);
}
