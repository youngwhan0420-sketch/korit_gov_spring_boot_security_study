package com_korit.security_study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CompareVerifyCodeReqDto {
    private Integer userId;
    private String verifyCode;
}