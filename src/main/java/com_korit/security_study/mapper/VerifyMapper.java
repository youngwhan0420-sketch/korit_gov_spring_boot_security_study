package com_korit.security_study.mapper;

import com_korit.security_study.entity.Verify;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface VerifyMapper {
    void addVerifyCode(Verify verify);
    Optional<Verify> getVerifyCode(Integer userId);
    void deleteVerifyCode(Integer userId);
}