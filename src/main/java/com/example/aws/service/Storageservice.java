package com.example.aws.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class Storageservice {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3client;

    public boolean uploadFile(MultipartFile File){
        File fileObj = convertMultiPartFiletoFile(File);
        String fileName = System.currentTimeMillis()+"_"+File.getOriginalFilename();
        try {
            PutObjectResult putObjectResult = s3client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            fileObj.delete();
            return true;
        } catch (AmazonServiceException e){
            return false;
        } catch (SdkClientException e){
            return false;
        }
    }

    public byte[] downloadFile(String fileName) throws IOException {

        S3Object s3Object = s3client.getObject(bucketName,fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteFile(String filename){
        s3client.deleteObject(bucketName,filename);
        return filename+ "removed......";

    }

    private File convertMultiPartFiletoFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){

            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  convertedFile;
    }

}
