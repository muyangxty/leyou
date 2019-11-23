package com.leyou.auth.service;

/**
 * 授权中心相关的业务层接口
 *
 * @author MuYang
 * @date 2019-11-23
 */
public interface IAuthService {

    /**
     * 生成token
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String accredit(String username, String password);
}
