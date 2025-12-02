package com_korit.security_study.controller;
import com_korit.security_study.dto.CompareVerifyCodeReqDto;
import com_korit.security_study.dto.ModifyPasswordReqDto;
import com_korit.security_study.dto.ModifyUsernameReqDto;
import com_korit.security_study.security.model.Principal;
import com_korit.security_study.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/pricipal")
    public ResponseEntity<?> getPrincipal(@AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @PostMapping("/modify/username")
    public ResponseEntity<?> modifyUsername(@RequestBody ModifyUsernameReqDto modifyUsernameReqDto, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.modifyUsername(modifyUsernameReqDto, principal));
    }

    @PostMapping("/modify/password")
    public ResponseEntity<?> modifyPassword(@RequestBody ModifyPasswordReqDto modifyPasswordReqDto, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.modifyPassword(modifyPasswordReqDto, principal));
    }

    @GetMapping("/verify/get")
    public ResponseEntity<?> getVerifyCode(@RequestParam Integer userId, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.getVerifyCode(userId, principal));
    }

    @PostMapping("/verify/compare")
    public ResponseEntity<?> compareVerifyCode(@RequestBody CompareVerifyCodeReqDto compareVerifyCodeReqDto, @AuthenticationPrincipal Principal principal) {
        return ResponseEntity.ok(accountService.compareVerifyCode(compareVerifyCodeReqDto, principal));
    }
}