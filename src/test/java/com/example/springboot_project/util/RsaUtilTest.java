package com.example.springboot_project.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Rsa 测试类
 *
 */
public class RsaUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(RsaUtilTest.class);

    // 公钥和私钥文件保存地址
    private String privateFilePath = "D:\\id_rsa";
    private String publicFilePath = "D:\\id_rsa_pub";

    private String privateFilePath1 = "D:\\id1_rsa";
    private String publicFilePath1 = "D:\\id1_rsa_pub";

    @Test
    public void test() throws Exception {
        // 生成密钥对
        RsaUtil.generateKey(publicFilePath1, privateFilePath1, "test", 2048);
        // 获取私钥
        PrivateKey privateKey = RsaUtil.getPrivateKey(privateFilePath1);
        logger.info("privateKey = {}", privateKey);
        // 获取公钥
        PublicKey publicKey = RsaUtil.getPublicKey(publicFilePath1);
        logger.info("publicKey = {}", publicKey);
    }
}
