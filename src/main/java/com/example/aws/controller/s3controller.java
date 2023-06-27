package com.example.aws.controller;

import com.example.aws.config.AuthRequest;
import com.example.aws.config.StorageCon;
import com.example.aws.entity.UserInfo;
import com.example.aws.service.JwtService;
import com.example.aws.service.Sservices;
import com.example.aws.service.Storageservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/rest")
public class s3controller {

    @Autowired
    private Storageservice service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private Sservices servicedb;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/upload")
    public ResponseEntity<Boolean> uploadFiles(@RequestParam(value = "file")MultipartFile file){
        return new ResponseEntity<Boolean>(service.uploadFile(file), HttpStatus.OK);
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFiles(@PathVariable String fileName) throws IOException {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return  ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition", "attachment; filename=\""+ fileName +"\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        return  new ResponseEntity<>(service.deleteFile(fileName),HttpStatus.OK);

    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        }
        else {
            throw new UsernameNotFoundException("invalid user request!");

        }


    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getMsg")
    public String getMsg(){
        return "COmes from authenicated process";
    }

//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getDsg")
    public String getDsg(){
        return "COmes ssfrom authenicated process";
    }

    @GetMapping("/freepass")
    public String freepass(){
        return "no authenicated process";
    }

    @PostMapping("/new")
    public String addnewUSer(@RequestBody UserInfo userInfo){
        return servicedb.addUser(userInfo);
    }

    //tosendfilefrom one server to another

    @PostMapping("/sendfile")
    public byte[] sendfiletoanotherserver() throws IOException {
        Path path = Paths.get("C://MainGraph.pdf");
        byte[] bytearr = Files.readAllBytes(path);
        return bytearr;

    }
}
