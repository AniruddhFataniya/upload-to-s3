package com.rspl.uploadtos3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
@Service
public class S3ServiceImpl implements S3Service{
    @Override
    public void uploadFile(S3Client client, String bucketName, String objectKey,  String objectPath) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            S3Waiter waiter = client.waiter();
//        HeadObjectRequest waitRequest = HeadObjectRequest.builder()
//                .bucket(bucketName)
//                .key(objectKey)
//                .build();

        System.out.println("filepath:"+objectPath);




        //WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(waitRequest);

        try{
            //client.putObject(putObjectRequest, RequestBody.fromBytes(getObjectFile(objectPath)));
            PutObjectResponse response = client.putObject(putObjectRequest, RequestBody.fromBytes(getObjectFile(objectPath)));
          System.out.println(">>>>>>>>>Object uploaded<<<<<<<<<<<" +response.eTag());
//            waiterResponse.matched().response().ifPresent(x -> {
//                // run custom code that should be executed after the upload file exists
//                System.out.println(">>>>>>>>>Object uploaded<<<<<<<<<<<" +response.eTag());
//            });

        }catch(S3Exception e){
            System.err.println(e.getMessage());
        }


    }

    @Override
    public void multipartUpload(AmazonS3 client, String bucketName, String objetctKey, String objectPath) throws IOException, InterruptedException {
        try {
            TransferManager transferManager = TransferManagerBuilder.standard()
                    .withS3Client(client)
                    .withMultipartUploadThreshold((long) (10485760)) //if size > 10 MB
                    .withMinimumUploadPartSize((long) (10485760))
                    .build();

            Upload upload = transferManager.upload(bucketName, objetctKey, new File(objectPath));
            System.out.println("Object upload started");
            upload.waitForCompletion();
            System.out.println("Object upload complete");
            transferManager.shutdownNow();
        } catch (Exception e){
            e.printStackTrace();
        }

//        CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
//                .bucket(bucketName)
//                .key(objetctKey)
//                .build();
//
//        CreateMultipartUploadResponse response = client.createMultipartUpload(createMultipartUploadRequest);
//        String uploadId = response.uploadId();
//        System.out.println(uploadId);
//
//        // Upload different parts of the object
//        UploadPartRequest uploadPartRequest1 = UploadPartRequest.builder()
//                .bucket(bucketName)
//                .key(objetctKey)
//                .uploadId(uploadId)
//                .partNumber(1)
//                .build();
//
//        String etag1 = client.uploadPart(uploadPartRequest1, RequestBody.fromBytes(getObjectFile(objectPath))).eTag();
//
//        CompletedPart part1 = CompletedPart.builder()
//                .partNumber(1)
//                .eTag(etag1)
//                .build();
//
//        UploadPartRequest uploadPartRequest2 = UploadPartRequest.builder()
//                .uploadId(uploadId)
//                .partNumber(2)
//                .build();
//
//        String etag2 = client.uploadPart(uploadPartRequest2, RequestBody.fromBytes(getObjectFile(objectPath))).eTag();
//
//        CompletedPart part2 = CompletedPart.builder()
//                .partNumber(2)
//                .build();
//

        


    }


    // Return a byte array.
    private static byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytesArray;
    }
}
