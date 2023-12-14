package com.example.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    UserInfoEntity findByUsername(String username);

    UserInfoEntity findByToken(String token);

    UserInfoEntity findByEmail(String email);

    List<UserInfoEntity> findAllByRolesContains(Role role);
}
