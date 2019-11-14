package com.leyou.user.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


/**
 * 加密工具类
 *
 * @author MuYang
 * @date 2019-11-13
 */
public class CodecUtils {


    /**
     * 进行MD5加密
     *
     * @param data 需要加密的数据
     * @param salt 盐
     * @return
     */
    public static String md5Hex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(data));
    }

    /**
     * 进行sha加密
     * @param data 需要加密的数据
     * @param salt 盐
     * @return
     */
    public static String shaHex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.sha512Hex(salt + DigestUtils.sha512Hex(data));
    }

    /**
     * 生成盐
     *
     * @return
     */
    public static String generateSalt() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
