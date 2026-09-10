package com.vsu.iscr.controller;

import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.domain.Users;
import com.vsu.iscr.service.UsersService;
import com.vsu.iscr.utils.ResultVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @author iscr
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UsersService usersService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVo login(@RequestBody Users loginUser) {
        try {
            // 查询用户
            Users user = usersService.findByUsername(loginUser.getUserName());

            if (user == null) {
                return ResultVoUtil.error("用户不存在");
            }

            // 验证密码（实际项目中应该使用加密密码）
            if (!user.getPassword().equals(loginUser.getPassword())) {
                return ResultVoUtil.error("密码错误");
            }

            // 返回用户信息（不包含密码）
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUId());
            data.put("username", user.getUserName());
            data.put("email", user.getEmail());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());

            return ResultVoUtil.success(data);
        } catch (Exception e) {
            log.error("登录失败", e);
            return ResultVoUtil.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResultVo register(@RequestBody Users user) {
        try {
            // 检查用户名是否已存在
            Users existUser = usersService.findByUsername(user.getUserName());
            if (existUser != null) {
                return ResultVoUtil.error("用户名已存在");
            }

            // 设置默认角色为普通用户
            if (user.getRole() == null) {
                user.setRole(0);
            }

            // 保存用户（实际项目中应该加密密码）
            int result = usersService.insertUser(user);
            if (result > 0) {
                return ResultVoUtil.success("注册成功");
            }
            return ResultVoUtil.error("注册失败");
        } catch (Exception e) {
            log.error("注册失败", e);
            return ResultVoUtil.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResultVo logout() {
        return ResultVoUtil.success("登出成功");
    }
}
