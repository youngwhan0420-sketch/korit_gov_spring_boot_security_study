package com_korit.security_study.controller;

import com_korit.security_study.dto.SendMailReqDto;
import com_korit.security_study.security.model.Principal;
import com_korit.security_study.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMail(@RequestBody SendMailReqDto sendMailReqDto, @AuthenticationPrincipal Principal principal) {
        System.out.println("11");
        return ResponseEntity.ok(mailService.sendMail(sendMailReqDto, principal));
    }
}
