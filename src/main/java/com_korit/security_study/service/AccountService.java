package com_korit.security_study.service;

import com_korit.security_study.dto.ApiRespDto;
import com_korit.security_study.dto.CompareVerifyCodeReqDto;
import com_korit.security_study.dto.ModifyPasswordReqDto;
import com_korit.security_study.dto.ModifyUsernameReqDto;
import com_korit.security_study.entity.User;
import com_korit.security_study.entity.UserRole;
import com_korit.security_study.entity.Verify;
import com_korit.security_study.repository.UserRepository;
import com_korit.security_study.repository.UserRoleRepository;
import com_korit.security_study.repository.VerifyRepository;
import com_korit.security_study.security.model.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerifyRepository verifyRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> modifyUsername(ModifyUsernameReqDto modifyUsernameReqDto, Principal principal) {
        if (!modifyUsernameReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(modifyUsernameReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "회원정보가 존재하지 않습니다.", null);
        }

        Optional<User> usernameFoundUser = userRepository.getUserByUsername(modifyUsernameReqDto.getUsername());
        if (usernameFoundUser.isPresent()) {
            return new ApiRespDto<>("failed", "이미 존재하는 사용자 이름입니다.", null);
        }

        int result = userRepository.modifyUsername(modifyUsernameReqDto.toEntity());
        if (result != 1) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다. 다시 시도해 주세요.", null);
        }
        return new ApiRespDto<>("success", "사용자이름이 변경되었습니다.", null);
    }

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        if (!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(modifyPasswordReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "회원정보가 존재하지 않습니다.", null);
        }

        if (!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요.", null);
        }

        if (bCryptPasswordEncoder.matches(modifyPasswordReqDto.getNewPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "새 비밀번호가 기존 비밀번호와 동일 할 수 없습니다.", null);
        }

        int result = userRepository.modifyPassword(modifyPasswordReqDto.toEntity(bCryptPasswordEncoder));
        if (result != 1) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다. 다시 시도해 주세요.", null);
        }
        return new ApiRespDto<>("success", "비밀번호가 변경되었습니다. 다시 로그인 해주세요.", null);
    }

    public ApiRespDto<?> getVerifyCode(Integer userId, Principal principal) {
        if (!userId.equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(userId);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "회원정보가 존재하지 않습니다.", null);
        }

        Optional<Verify> foundVerify = verifyRepository.getVerifyCode(userId);

        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다. 인증코드를 다시 전송하세요.", null);
        }
        return new ApiRespDto<>("success", "이메일 인증 코드가 전송되었습니다.", foundVerify.get());
    }

    public ApiRespDto<?> compareVerifyCode(CompareVerifyCodeReqDto compareVerifyCodeReqDto, Principal principal) {
        if (!compareVerifyCodeReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(compareVerifyCodeReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "회원정보가 존재하지 않습니다.", null);
        }

        Optional<Verify> foundVerify = verifyRepository.getVerifyCode(compareVerifyCodeReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다. 인증코드를 다시 전송하세요.", null);
        }

        if (!compareVerifyCodeReqDto.getVerifyCode().equals(foundVerify.get().getVerifyCode())) {
            return new ApiRespDto<>("failed", "인증코드가 일치하지 않습니다.", null);
        }

        List<UserRole> userRoles = userRoleRepository.getUserRoleList(compareVerifyCodeReqDto.getUserId());
        Optional<UserRole> foundUserRole = userRoles.stream()
                .filter(userRole -> userRole.getRoleId().equals(3))
                .findFirst();
        if (foundUserRole.isEmpty()) {
            return new ApiRespDto<>("failed", "인증이 필요하지 않은 계정입니다.", null);
        }

        UserRole userRole = foundUserRole.get();
        userRole.setRoleId(2);

        int result = userRoleRepository.updateUserRole(userRole);
        if (result != 1) {
            return new ApiRespDto<>("failed", "문제가 발생했습니다. 다시 시도해주세요.", null);
        }

        verifyRepository.deleteVerifyCode(foundVerify.get().getUserId());

        return new ApiRespDto<>("success", "이메일 인증이 완료되었습니다.", null);
    }
}