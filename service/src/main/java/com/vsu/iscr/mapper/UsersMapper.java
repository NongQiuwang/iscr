package com.vsu.iscr.mapper;

import com.vsu.iscr.domain.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问接口
 *
 */
@Mapper
public interface UsersMapper {

    /**
     * 根据用户名查找用户
     */
    Users findByUsername(@Param("username") String username);

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
    Users selectUserById(@Param("userId") Integer userId);

    /**
     * 更新用户信息
     */
    int updateUser(Users user);

    /**
     * 删除用户
     */
    int deleteUserById(@Param("userId") Integer userId);
}
