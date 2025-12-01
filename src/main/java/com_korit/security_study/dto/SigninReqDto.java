package com_korit.security_study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SigninReqDto {
    private String username;
    private String password;
    private String email;


}