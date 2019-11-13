package com.leyou.user.service.impl;


import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.IUserService;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 用户微服务业务层实现类
 *
 * @author MuYang
 * @Date 2019-11-13
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:verify:";

    static final Logger logger = LoggerFactory.getLogger(IUserService.class);

    /**
     * 校验数据是否可用
     *
     * @param data 要检验的数据
     * @param type 要校验的数据类型，1-用户名，2-手机
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        //判断要校验的数据类型
        switch (type) {
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

    /**
     * 发送验证码
     *
     * @param phone 手机
     */
    @Override
    public void sendVeifyCode(String phone) {
        if (StringUtils.isBlank(phone)) {
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);

        //发送消息到rabbitMQ
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "verifycode.sms", msg);

        //把验证码保存到redis中
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     *
     * @param user 用户数据
     * @param code 验证码
     */
    @Override
    public void register(User user, String code) {
        //查询redis中的验证码
        String redisCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (StringUtils.equals(code, redisCode)) {
            return;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
    }

    /**
     * 根据用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        //判断以用户名查询结果是否为null
        if (user == null) {
            return null;
        }
        //获取盐，对用户输入的密码进行加密
        password = CodecUtils.md5Hex(password, user.getSalt());
        //判断密码是否正确
        if (StringUtils.equals(password, user.getPassword())) {
            return user;
        }
        return null;
    }


}
