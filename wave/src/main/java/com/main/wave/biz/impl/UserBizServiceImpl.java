package com.main.wave.biz.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.main.wave.biz.UserBizService;
import com.main.wave.dto.mapper.UserMapper;
import com.main.wave.dto.request.user.LoginRequest;
import com.main.wave.dto.request.user.RegisterRequest;
import com.main.wave.entity.User;
import com.main.wave.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBizServiceImpl implements UserBizService {

    @Autowired
    private UserService userService;

    @Override
    public boolean login(LoginRequest request) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda()
                .eq(User::getName, request.getName())
                .eq(User::getPassword, SecureUtil.md5(request.getPassword()));
        return userService.exists(queryWrapper);
    }

    @Override
    public boolean register(RegisterRequest request) {
        LambdaQueryWrapper<User> query = new QueryWrapper<User>().lambda()
                .eq(User::getName, request.getName());
        boolean exists =   userService.exists(query);
        if(exists) return false;
        User user = UserMapper.INSTANCE.registerRequestToUser(request);
        user.setPassword(SecureUtil.md5(user.getPassword()));
        return userService.save(user);
    }

}
