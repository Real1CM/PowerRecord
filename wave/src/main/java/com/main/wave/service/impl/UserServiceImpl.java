package com.main.wave.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.main.wave.entity.User;
import com.main.wave.mapper.UserMapper;
import com.main.wave.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
