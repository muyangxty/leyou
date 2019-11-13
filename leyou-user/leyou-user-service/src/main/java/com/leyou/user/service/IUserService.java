package com.leyou.user.service;

import com.leyou.user.pojo.User;

public interface IUserService {

    /**
     * 校验数据是否可用
     * @param data 要检验的数据
     * @param type 要校验的数据类型，1-用户名，2-手机
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送验证码
     * @param phone 手机
     */
    void sendVeifyCode(String phone);

    /**
     * 用户注册
     * @param user 用户数据
     * @param code 验证码
     */
    void register(User user, String code);

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    User queryUser(String username, String password);
}
