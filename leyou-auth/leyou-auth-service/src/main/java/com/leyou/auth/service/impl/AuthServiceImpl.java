package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.IAuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 授权中心相关的业务层实现类
 *
 * @author MuYang
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserClient userClient;

    @Override
    public String accredit(String username, String password) {
        //根据用户名和密码查询
        User user = this.userClient.queryUser(username, password);
        //判断user是否为null
        if (user == null) {
            return null;
        }

        try {
            UserInfo userInfo = new UserInfo();
            //将用户的id设置为载荷的id
            userInfo.setId(user.getId());
            //将用户的名字设置为载荷的名字
            user.setUsername(user.getUsername());
            //通过jwtUtils生成jwt类型的token
            return JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
