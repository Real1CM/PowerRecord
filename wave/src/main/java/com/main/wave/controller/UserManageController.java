package com.main.wave.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.main.wave.dto.BaseResponse;
import com.main.wave.dto.Response;
import com.main.wave.dto.mapper.UserMapper;
import com.main.wave.dto.request.user.RegisterRequest;
import com.main.wave.dto.request.user.UpdateUserRequest;
import com.main.wave.entity.User;
import com.main.wave.service.UserService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/manage")
public class UserManageController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表
     * @param pageNumber 当前页数
     */
    @GetMapping("/page")
    public BaseResponse<Page<User>> userPage(@RequestParam("page") Long pageNumber) {
        Page<User> page = userService.page(new Page<>(pageNumber, 10));
        return Response.success(page);
    }


    @GetMapping("/{id}")
    public BaseResponse<User> userInfo(@PathVariable Long id) {
        User user = userService.getById(id);
        return Response.success(user);
    }



    @PostMapping("/add")
    public BaseResponse<Page<User>> addUser(@RequestBody RegisterRequest user) {
        User addUser = UserMapper.INSTANCE.registerRequestToUser(user);
        addUser.setPassword(SecureUtil.md5(addUser.getPassword()));
        boolean success = userService.save(addUser);
        return success ? Response.success() : Response.error();
    }

    /**
     * 删除指定Id用户
     * @param id id
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteUser(@PathVariable Long id) {
        boolean b = userService.removeById(id);
        return Response.success(b);
    }

    /**
     * 更新指定id用户信息
     * @param request 用户信息
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UpdateUserRequest request) {
        User user = UserMapper.INSTANCE.updateRequestToUser(request);
        if(Strings.isBlank(user.getPassword())) {
            user.setPassword(null);
        }
        boolean b = userService.updateById(user);
        return Response.success(b);
    }

}
