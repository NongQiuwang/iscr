package com.vsu.iscr.service.impl;

import com.vsu.iscr.domain.Users;
import com.vsu.iscr.mapper.UsersMapper;
import com.vsu.iscr.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务逻辑实现类
 *
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

    @Override
    public int insertUser(Users user) {
        return usersMapper.insertUser(user);
    }

    @Override
    public List<Users> selectAllUsers() {
        return usersMapper.selectAllUsers();
    }

    @Override
    public Users selectUserById(Integer userId) {
        return usersMapper.selectUserById(userId);
    }

    @Override
    public int updateUser(Users user) {
        return usersMapper.updateUser(user);
    }

    @Override
    public int deleteUserById(Integer userId) {
        return usersMapper.deleteUserById(userId);
    }
}
