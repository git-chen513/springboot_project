package com.example.springboot_project.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * JWT 测试类
 *
 */
public class JwtUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtilTest.class);

    // 公钥和私钥文件保存地址
    private String privateFilePath = "D:\\id_rsa";
    private String publicFilePath = "D:\\id_rsa_pub";

    private String privateFilePath1 = "D:\\id1_rsa";
    private String publicFilePath1 = "D:\\id1_rsa_pub";

    @Test
    public void generateTest() {
        String token = JwtUtil.generate("admin");
        logger.info("生成的jwt token为：{}", token);
    }

    @Test
    public void parseTest() {
        Claims cla = JwtUtil.parse("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3MDkwMzI5NCwiZXhwIjoxNjcwOTEwNDk0fQ.7SBFqlSHH4MK6qelHTMGTd1CEIKe1b1yw01iTZg8JkEBbbqSpnjg5IhGXBqVWyphVyf--o99qzLbCYr7CLZYtA");
    }

    @Test
    public void generateJwtTokenTest() throws Exception {
        // 获取私钥
        PrivateKey privateKey = RsaUtil.getPrivateKey(privateFilePath);
        // 私钥加密，生成jwt token
        String token = JwtUtil.generateJwtToken("admin", privateKey);
        logger.info("通过非对称加密算法生成的jwt token为：{}", token);
    }

    @Test
    public void parserJwtTokenTest() throws Exception {
        // 获取公钥
        PublicKey publicKey = RsaUtil.getPublicKey(publicFilePath);
        // 公钥解密，解析jwt token中的用户信息
        String token  = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3MDkyMDQ0MSwiZXhwIjoxNjcwOTI3NjQxfQ.ArCC8XpKagVu5x464Us4X27ndB3R9FmLQlLUaIak_cjFe0ttXC33yiU1EwTtnzlKihlnodxCmRqO_bmxCAAjOWXIa3pkMEPRrG9lFGQlBMk_E5s2vCdhwBwoNiqYlx7QOC3Ltn4W9_SxSsrviQO7pKY9Um61NVk9uQrk3bPEE6FoC6lRynApwOhbg-s3gfxI-nFNQqFYGd3VDO3xp6uIyjbEyKDu-VfUaHUg-Z1S_iYzorl6iD6f9ide-_5IBYh-yCtBOK1hrEmg3E-6QsQt-4Y1WOSzaVIruiBqPWmHMtftqjaZjtuCYfKiyhLMZthRgytn4Y4NbfT-R-2y_q7vNA";
        Claims claims = JwtUtil.parserJwtToken(token, publicKey);
        String subject = claims.getSubject();
        logger.info("用户名为：{}", subject);
    }
}
