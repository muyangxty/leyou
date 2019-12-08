package com.leyou.user.controller;


import com.leyou.user.pojo.User;
import com.leyou.user.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 用户微服务控制器层
 *
 * @author MuYang
 * @Date 2019-12-07
 */
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 校验数据是否可用
     *
     * @param data 要校验的数据
     * @param type 要校验的类型，1-用户名，2-手机
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        Boolean bool = this.userService.checkUser(data, type);
        //判断校验结果是否为null
        if (bool == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }

    /**
     * 发送验证码
     *
     * @param phone 手机
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone) {
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 用户注册
     *
     * @param user 用户数据
     * @param code 验证码
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> userRegister(@Valid User user, @RequestParam("code") String code) {
        this.userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = this.userService.queryUser(username, password);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

}
