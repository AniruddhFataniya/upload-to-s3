package com.rspl.uploadtos3;

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
