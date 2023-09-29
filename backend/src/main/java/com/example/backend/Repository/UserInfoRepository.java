package com.example.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    List<UserInfoEntity> findByUsername(String username);
}
