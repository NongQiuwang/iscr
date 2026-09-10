package com.vsu.iscr.controller;

import com.vsu.iscr.core.vo.ResultVo;
import com.vsu.iscr.domain.Users;
import com.vsu.iscr.service.UsersService;
import com.vsu.iscr.utils.ResultVoUtil;
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
            user.setPassword(null); // 安全起见，隐藏密码
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
