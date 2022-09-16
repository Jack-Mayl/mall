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

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     * @throws LongZaiMallException
     */
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

    /**
     * 登陆
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws LongZaiMallException
     */
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

    /**
     * 更新个性签名
     * @param session
     * @param signature
     * @return
     * @throws LongZaiMallException
     */
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

    /**
     * 登出清除Session
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.LONGZAI_MALL_USER);
        return ApiRestResponse.success();

    }

    /**
     * 管理员登陆接口
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws LongZaiMallException
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password, HttpSession session) throws LongZaiMallException {
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_USER_NAME);
        }

        if(StringUtils.isEmpty(password)){
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_PASSWORD);
        }
        User login = userService.login(userName, password);
        // 校验是否是管理员
        if (userService.checkAdminRole(login)) {
            // 是管理员 执行操作
            // 保存用户信息时，不保存密码
            login.setPassword(null);
            session.setAttribute(Constant.LONGZAI_MALL_USER,login);
            return ApiRestResponse.success(login);
        }else{
            return ApiRestResponse.error(LongZaiMallExceptionEnum.NEED_ADMIN);
        }

    }
}
