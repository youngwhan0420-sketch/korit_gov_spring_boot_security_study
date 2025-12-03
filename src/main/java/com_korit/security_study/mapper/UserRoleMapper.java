package com_korit.security_study.mapper;

import com_korit.security_study.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserRoleMapper {
    void addUserRole(UserRole userRole);
    List<UserRole> getUserRoleList(Integer userId);
    void updateUserRole(UserRole userRole);
}