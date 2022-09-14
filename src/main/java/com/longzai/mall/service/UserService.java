package com.longzai.mall.service;

import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.model.pojo.User;

public interface UserService {
    User getUser();

    void register(String userName,String passWord) throws LongZaiMallException;
}