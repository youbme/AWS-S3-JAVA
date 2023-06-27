package com.example.aws.service;

import com.example.aws.entity.UserInfo;
import com.example.aws.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Sservices {

    @Autowired
    private  UserInfoRepo userrepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userrepo.save(userInfo);
        return "user added to system";

    }
}
