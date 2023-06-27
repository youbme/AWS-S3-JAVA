package com.example.aws;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;

public class GeneratePresignedUrl {

    public static void main(String[] args){
    //providing credentials data

        final String bucketName= "test-bucket-sclera";
        final String keyName ="vdms911/craggerfaker.sql";


        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_1;
        //builderpresigner using builder
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();

        signBucket(presigner,bucketName,keyName);
        presigner.close();


    }


    public static void signBucket(S3Presigner presigner, String bucketName, String keyName){
        File file= new File("C://datagenerator.sql");
        try{
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("application/octet-stream")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(2))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myUrl = presignedRequest.url().toString();

            System.out.println("Presigned URL to upload a file to: " + myUrl);
            System.out.println("Which Http method need to be used when uploading afile:" + presignedRequest.httpRequest().method());

            //upload content to the amazon s3 bucket by using this
            URL url = presignedRequest.url();

            //Create the connection and use it to upload the new  object by using the presigned URL
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/octet-stream");
            connection.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            InputStream is = new FileInputStream(file);

            int i =0 ;
            //read byte by byte until end of stream
            while ((i = is.read())!=-1){
                out.write(i);
               // System.out.println(i);
            }
            out.flush();
            is.close();
            out.close();
            System.out.println("yooo");



            System.out.println("HTTP response code is "+ connection.getResponseCode());



        } catch(S3Exception | IOException e){
            e.getStackTrace();

        }


    }
}
