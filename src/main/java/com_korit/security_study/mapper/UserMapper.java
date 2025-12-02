package com_korit.security_study.mapper;

import com_korit.security_study.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@Mapper

public interface UserMapper {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByUserId(Integer userId);
    Optional<User> getUserByEmail(String email);
    void addUser(User user);
    int modifyUsername(User user);
    int modifyPassword(User user);
    int updatePassword(User user);
}