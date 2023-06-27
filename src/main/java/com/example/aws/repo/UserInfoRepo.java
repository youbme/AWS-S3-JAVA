package com.example.aws.repo;

import com.example.aws.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo,Integer>{

    Optional<UserInfo> findByName(String username);

    UserInfo save(UserInfo userInfo);
}
