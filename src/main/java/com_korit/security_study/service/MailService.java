package com_korit.security_study.service;

import com_korit.security_study.dto.ApiRespDto;
import com_korit.security_study.dto.SendMailReqDto;
import com_korit.security_study.entity.User;
import com_korit.security_study.repository.UserRepository;
import com_korit.security_study.security.jwt.JwtUtils;
import com_korit.security_study.security.model.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class MailService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender javaMailSender;

    public ApiRespDto<?> sendMail(SendMailReqDto sendMailReqDto, Principal principal) {
        if (!sendMailReqDto.getEmail().equals(principal.getEmail())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }
        Optional<User> foundUser = userRepository.getUserByEmail(sendMailReqDto.getEmail());

        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요", null);
        }
        User user = foundUser.get();

        boolean hasTempRole = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRoleId() == 3);
        if (!hasTempRole) {
            return new ApiRespDto<>("failed", "인증이 필요한 계정이 아닙니다.", null);
        }

        String token = jwtUtils.generateVerifyToken(user.getUserId().toString());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendMailReqDto.getEmail());
        message.setSubject(" [이메일 인증] 이메일을 인증해주세요."); //이메일 제목
        message.setText("링크를 클릭해 인증을 완료해주세요.: " +
                "http://localhost:8080/mail/veridy?verifyToken=" + token);//링크 클릭으로 인증하게 하기

        javaMailSender.send(message);
        return new ApiRespDto<>("success", "인증 메일이 전송되었습니다. 메일을 확인하세요", null);


    }
}
