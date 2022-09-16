package com.longzai.mall.service.impl;

import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.exception.LongZaiMallExceptionEnum;
import com.longzai.mall.model.dao.UserMapper;
import com.longzai.mall.model.pojo.User;
import com.longzai.mall.service.UserService;
import com.longzai.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

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
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.NAME_EXISTED);
        }
        // 写到数据库
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(passWord));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int count = userMapper.insertSelective(user);
        if(count==0){
            throw  new LongZaiMallException(LongZaiMallExceptionEnum.INSERT_FAILED);
        }
    }
    @Override
    public User login(String userName, String password) throws LongZaiMallException {
        String md5PassWord = null;
        try {
            md5PassWord = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5PassWord);
        if(user==null){
            throw new LongZaiMallException(LongZaiMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;

    }

    @Override
    public  void updateInformation(User user) throws LongZaiMallException {
        // 更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount >1){
            throw new LongZaiMallException(LongZaiMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        // 1.是普通用户 2是管理员
        return  user.getRole().equals(2);
    }
}
