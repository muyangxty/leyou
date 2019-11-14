package com.leyou.auth.service;

public interface IAuthService {

    /**
     * 生成token
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String accredit(String username, String password);
}
