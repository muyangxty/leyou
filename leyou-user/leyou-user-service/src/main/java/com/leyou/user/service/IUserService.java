package com.leyou.user.service;

public interface IUserService {

    /**
     * 校验数据是否可用
     * @param data 要检验的数据
     * @param type 要校验的数据类型，1-用户名，2-手机
     * @return
     */
    Boolean checkUser(String data, Integer type);
}
