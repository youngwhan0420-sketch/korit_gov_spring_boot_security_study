package com_korit.security_study.repository;

import com_korit.security_study.entity.UserRole;
import com_korit.security_study.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class UserRoleRepository {

    @Autowired
    private UserRoleMapper userRoleMapper;

    public void addUserRole(UserRole userRole) {
        userRoleMapper.addUserRole(userRole);
    }

    public List<UserRole> getUserRoleList(Integer userId) {
        return userRoleMapper.getUserRoleList(userId);
    }

    public int updateUserRole(UserRole userRole) {
        return userRoleMapper.updateUserRole(userRole);
    }
}