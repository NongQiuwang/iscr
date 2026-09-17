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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsersController {

    @Autowired
    public UsersService usersService;

    /**
     * 获取所有用户 (仅管理员)
     */
    @GetMapping("/all")
    public ResultVo getAllUsers() {
        try {
            List<Users> list = usersService.selectAllUsers();
            if (list != null) {
                for (Users u : list) {
                    u.setPassword(null);
                }
            }
            return ResultVoUtil.success(list);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return ResultVoUtil.error("获取失败");
        }
    }

    /**
     * 获取单个用户信息
     */
    @GetMapping("/{id}")
    public ResultVo getUserById(@PathVariable("id") Integer id) {
        try {
            Users user = usersService.selectUserById(id);
            if (user == null)
                return ResultVoUtil.error("用户不存在");
            user.setPassword(null);
            return ResultVoUtil.success(user);
        } catch (Exception e) {
            return ResultVoUtil.error("获取失败");
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResultVo updateUser(@PathVariable("id") Integer id, @RequestBody Users user) {
        try {
            user.setUId(id);
            String email = user.getEmail() == null ? null : user.getEmail().trim();
            String phone = user.getPhone() == null ? null : user.getPhone().trim();

            if (email != null && !email.isEmpty() && !Validators.isEmail(email)) {
                return ResultVoUtil.error("邮箱格式不正确");
            }
            if (phone != null && !phone.isEmpty() && !Validators.isPhone(phone)) {
                return ResultVoUtil.error("手机号格式不正确");
            }
            if (email != null && !email.isEmpty()) {
                Users exist = usersService.findByEmail(email);
                if (exist != null && !exist.getUId().equals(id)) {
                    return ResultVoUtil.error("邮箱已被注册");
                }
            }
            if (phone != null && !phone.isEmpty()) {
                Users exist = usersService.findByPhone(phone);
                if (exist != null && !exist.getUId().equals(id)) {
                    return ResultVoUtil.error("手机号已被注册");
                }
            }

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(BCrypt.hashpw(user.getPassword()));
            } else {
                user.setPassword(null);
            }

            user.setEmail(email);
            user.setPhone(phone);

            int result = usersService.updateUser(user);
            if (result > 0)
                return ResultVoUtil.success("更新成功");
            return ResultVoUtil.error("更新失败");
        } catch (Exception e) {
            return ResultVoUtil.error("更新出错");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResultVo deleteUser(@PathVariable("id") Integer id) {
        try {
            int result = usersService.deleteUserById(id);
            if (result > 0)
                return ResultVoUtil.success("删除成功");
            return ResultVoUtil.error("删除失败");
        } catch (Exception e) {
            return ResultVoUtil.error("删除出错");
        }
    }
}
