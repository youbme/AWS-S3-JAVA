package com.example.aws;

import software.amazon.awssdk.services.s3.waiters.S3Waiter;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

import java.io.IOException;
import java.io.InputStream;

public class S3Util {

    public static void main (String[] args){

            String bucketName = "test-bucket-sclera";
            S3Client client = S3Client.builder().build();
            S3Waiter waiter = client.waiter();

            CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucketName).build();
            client.createBucket(request);
        HeadBucketRequest requestWait = HeadBucketRequest.builder().bucket(bucketName).build();
            waiter.waitUntilBucketExists(requestWait);
        System.out.println("Bucket "+ bucketName+" is ready.");
    }
}
