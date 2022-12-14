package com.example.springboot_project.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Rsa 测试类
 *
 */
public class RsaUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(RsaUtilTest.class);

    // 公钥和私钥文件保存地址
    private String privateFilePath = "D:\\private.txt";
    private String publicFilePath = "D:\\public.txt";

    @Test
    public void test() throws Exception {
        // 生成密钥对
        RsaUtil.generateKey(publicFilePath, privateFilePath, "test", 2048);
        // 获取私钥
        PrivateKey privateKey = RsaUtil.getPrivateKey(privateFilePath);
        logger.info("privateKey = {}", privateKey);
        // 获取公钥
        PublicKey publicKey = RsaUtil.getPublicKey(publicFilePath);
        logger.info("publicKey = {}", publicKey);
    }

    @Test
    public void testResourcesFile() throws Exception {
        File file = ResourceUtils.getFile("classpath:public.txt");
        String publicPath = file.getPath();
        logger.info("{}", publicPath);
        String privatePath = this.getClass().getClassLoader().getResource("private.txt").getPath();
        logger.info("{}", privatePath);
        PublicKey publicKey = RsaUtil.getPublicKey(publicPath);
        logger.info("{}", publicKey);
        PrivateKey privateKey = RsaUtil.getPrivateKey(privatePath);
        logger.info("{}", privateKey);
    }
}
