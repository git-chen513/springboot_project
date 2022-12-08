package com.example.springboot_project.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder 密码加密器测试类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/7 01:25
 */
public class PasswordEncoderTest {

    Logger logger = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Test
    public void bCryptPasswordEncoderTest() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        logger.info("原始密码为：{}", rawPassword);
        // 每次运行，加密的密码都不同
        String encodePassword = passwordEncoder.encode(rawPassword);
        logger.info("加密后的密码为：{}", encodePassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodePassword);
        logger.info("原始密码和加密后的密码是否匹配：{}", matches);
        logger.info("12345678和加密后的密码是否匹配：{}", passwordEncoder.matches("12345678", encodePassword));
    }
}
