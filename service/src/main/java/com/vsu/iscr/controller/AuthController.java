package com.vsu.iscr.controller;

import cn.hutool.crypto.digest.BCrypt;
import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.domain.Users;
import com.vsu.iscr.service.UsersService;
import com.vsu.iscr.utils.ResultVoUtil;
import com.vsu.iscr.utils.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
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
            if (Validators.isBlank(loginUser.getUserName()) || Validators.isBlank(loginUser.getPassword())) {
                return ResultVoUtil.error("用户名和密码不能为空");
            }

            Users user = usersService.findByUsername(loginUser.getUserName().trim());
            if (user == null) {
                return ResultVoUtil.error("用户不存在");
            }

            if (!passwordMatches(loginUser.getPassword(), user)) {
                return ResultVoUtil.error("密码错误");
            }

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
            String userName = user.getUserName() == null ? null : user.getUserName().trim();
            String password = user.getPassword();
            String email = user.getEmail() == null ? null : user.getEmail().trim();
            String phone = user.getPhone() == null ? null : user.getPhone().trim();

            if (Validators.isBlank(userName)) {
                return ResultVoUtil.error("用户名不能为空");
            }
            if (userName.length() < 2 || userName.length() > 20) {
                return ResultVoUtil.error("用户名长度需在2-20个字符之间");
            }
            if (Validators.isBlank(password)) {
                return ResultVoUtil.error("密码不能为空");
            }
            if (password.length() < 6 || password.length() > 64) {
                return ResultVoUtil.error("密码长度需在6-64位之间");
            }
            if (Validators.isBlank(email)) {
                return ResultVoUtil.error("邮箱不能为空");
            }
            if (!Validators.isEmail(email)) {
                return ResultVoUtil.error("邮箱格式不正确");
            }
            if (Validators.isBlank(phone)) {
                return ResultVoUtil.error("手机号不能为空");
            }
            if (!Validators.isPhone(phone)) {
                return ResultVoUtil.error("手机号格式不正确");
            }

            if (usersService.findByUsername(userName) != null) {
                return ResultVoUtil.error("用户名已存在");
            }
            if (usersService.findByEmail(email) != null) {
                return ResultVoUtil.error("邮箱已被注册");
            }
            if (usersService.findByPhone(phone) != null) {
                return ResultVoUtil.error("手机号已被注册");
            }

            user.setUserName(userName);
            user.setEmail(email);
            user.setPhone(phone);
            // 密码加密存储
            user.setPassword(BCrypt.hashpw(password));
            // 公开注册一律为普通用户，防止越权
            user.setRole(0);

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

    /**
     * 校验密码。
     * 兼容历史明文密码：首次用明文校验通过后自动升级为 BCrypt 加密。
     */
    private boolean passwordMatches(String rawPassword, Users user) {
        String stored = user.getPassword();
        if (stored == null) {
            return false;
        }
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return BCrypt.checkpw(rawPassword, stored);
        }
        if (stored.equals(rawPassword)) {
            usersService.updatePassword(user.getUId(), BCrypt.hashpw(rawPassword));
            return true;
        }
        return false;
    }
}
