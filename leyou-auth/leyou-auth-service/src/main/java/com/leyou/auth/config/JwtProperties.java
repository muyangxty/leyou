package com.leyou.auth.config;

import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * JWT配置类
 *
 * @author MuYang
 * @date 2019-11-23
 */
@Data
//读取配置文件信息，以leyou.jwt前缀
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
    // 密钥
    private String secret;
    // 公钥
    private String pubKeyPath;
    // 私钥
    private String priKeyPath;
    // token过期时间
    private Integer expire;
    // cookie名字
    private String cookieName;
    // 公钥
    private PublicKey publicKey;
    // 私钥
    private PrivateKey privateKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init() {
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            //判断公钥或者私钥是否不存在
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

    // getter setter ...
}