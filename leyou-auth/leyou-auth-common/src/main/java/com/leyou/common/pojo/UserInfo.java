package com.leyou.common.pojo;

import lombok.Data;

/**
 * 载荷对象
 *
 * @author MuYang
 * @Date 2019-11-14
 */

@Data
public class UserInfo {

    private Long id;

    private String username;

    public UserInfo() {
    }

    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}