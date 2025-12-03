package com_korit.security_study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class VerifyCodeReqDto {
    private String email;
}
