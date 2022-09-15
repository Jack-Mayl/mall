package com.longzai.mall.controller;

import com.longzai.mall.common.ApiRestResponse;
import com.longzai.mall.common.Constant;
import com.longzai.mall.exception.LongZaiMallException;
import com.longzai.mall.exception.LongZaiMallExceptionEnum;
import com.longzai.mall.model.pojo.User;
import com.longzai.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 描述： 用户控制器
 */
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage(){
        return userService.getUser();
    }
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam String userName,@RequestParam String password) throws LongZaiMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_USER_NAME);
        }

        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_PASSWORD);
        }
        // 密码长度不能少于8位
        if (password.length()<8){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName,password);
        return ApiRestResponse.success();
    }
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password, HttpSession session) throws LongZaiMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_USER_NAME);
        }

        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_PASSWORD);
        }
        User login = userService.login(userName, password);
        // 保存用户信息时，不保存密码
        login.setPassword(null);
        session.setAttribute(Constant.LONGZAI_MALL_USER,login);
        return ApiRestResponse.success(login);
    }
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session,@RequestParam("signature") String signature) throws LongZaiMallException {
        User currentUser = (User)session.getAttribute(Constant.LONGZAI_MALL_USER);
        if(currentUser == null){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.LONGZAI_MALL_USER);
        return ApiRestResponse.success();

    }
}
