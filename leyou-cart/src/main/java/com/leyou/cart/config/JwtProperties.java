package com.leyou.cart.config;

import com.leyou.common.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
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
    // 公钥
    private String pubKeyPath;
    // cookie名字
    private String cookieName;
    // 公钥
    private PublicKey publicKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init() {
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

}