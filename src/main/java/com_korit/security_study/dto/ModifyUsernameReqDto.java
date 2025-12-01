package com_korit.security_study.dto;

import com_korit.security_study.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyUsernameReqDto {
    private Integer userId;
    private String username;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}