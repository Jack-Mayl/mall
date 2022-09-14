package com.longzai.mall.service.impl;

import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.exception.LongZaiMallExceptionEnum;
import com.longzai.mall.model.dao.UserMapper;
import com.longzai.mall.model.pojo.User;
import com.longzai.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/*
描述 ： UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
       return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String userName, String passWord) throws LongZaiMallException {
        // 查询用户名是否存在 不允许重名
        User result = userMapper.selectByName(userName);
        if(result != null){
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.NEED_USER_NAME);
        }
        // 写到数据库
        User user = new User();
        user.setUsername(userName);
        user.setPassword(passWord);
        int count = userMapper.insertSelective(user);
        if(count==0){
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.INSERT_FAILED);
        }

    }
}
