package com.leyou.user.service.impl;


import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.IUserService;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 校验数据是否可用
     * @param data 要检验的数据
     * @param type 要校验的数据类型，1-用户名，2-手机
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        //判断要校验的数据类型
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return this.userMapper.selectCount(record) == 0;
    }
}
