package com_korit.security_study.repository;

import com_korit.security_study.entity.Verify;
import com_korit.security_study.mapper.VerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class VerifyRepository {

    @Autowired
    private VerifyMapper verifyMapper;

    public void addVerifyCode(Verify verify) {
        verifyMapper.addVerifyCode(verify);
    }

    public Optional<Verify> getVerifyCode(Integer userId) {
        return verifyMapper.getVerifyCode(userId);
    }

    public void deleteVerifyCode(Integer userId) {
        verifyMapper.deleteVerifyCode(userId);
    }
}