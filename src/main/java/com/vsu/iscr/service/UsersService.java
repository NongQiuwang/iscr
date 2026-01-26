package com.vsu.iscr.service;

import com.vsu.iscr.domain.Users;

import java.util.List;

/**
 * 用户业务接口
 *
 */
public interface UsersService {

    /**
     * 根据用户名查找用户
     */
    Users findByUsername(String username);

    /**
     * 新增用户
     */
    int insertUser(Users user);

    /**
     * 查询所有用户
     */
    List<Users> selectAllUsers();

    /**
     * 根据ID查询用户
     */
    Users selectUserById(Integer userId);

    /**
     * 更新用户信息
     */
    int updateUser(Users user);

    /**
     * 删除用户
     */
    int deleteUserById(Integer userId);
}
