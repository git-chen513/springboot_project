package com.example.springboot_project.util;

import com.example.springboot_project.constants.Constants;
import com.example.springboot_project.model.UserDetailsModel;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.ResourceUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * JWT 测试类
 *
 */
public class JwtUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtilTest.class);

    // 公钥和私钥文件保存地址
    private String privateFilePath = "classpath:private.txt";
    private String publicFilePath = "classpath:public.txt";

    private UserDetailsModel getUserDetailsModel() {
        UserDetailsModel userDetailsModel = new UserDetailsModel();
        userDetailsModel.setId(1);
        userDetailsModel.setUserName("admin");
        userDetailsModel.setPassword("$2a$10$S/9YZMsBQt9OPaqunB5M8ec7nhAFr0/vsElSyIsHG3gQaS8F5MnQ2");
        userDetailsModel.setPhone("15217761659");
        userDetailsModel.setEmail("123456@qq.com");
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
        Collection<GrantedAuthority> collection = new HashSet<>();
        collection.add(simpleGrantedAuthority);
        userDetailsModel.setAuthorities(collection);
        return userDetailsModel;
    }

    @Test
    public void generateTest() {
        String token = JwtUtil.generate(getUserDetailsModel());
        logger.info("通过对称加密算法生成的jwt token为：{}", token);
    }

    @Test
    public void parseTest() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyIjp7InBob25lIjoiMTUyMTc3NjE2NTkiLCJpZCI6MSwidXNlck5hbWUiOiJhZG1pbiIsImVtYWlsIjoiMTIzNDU2QHFxLmNvbSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX2FkbWluIn1dfSwic3ViIjoiYWRtaW4iLCJpYXQiOjE2NzA5ODg4MjMsImV4cCI6MTY3MDk5NjAyM30.GIcki3yW6KFtnuME-a8jTtzz_SizSgywRuZONredWA-mo5G1JepdHuWjlq6ew05YK73ReYLBQArAjIAIA_7uOA";
        Claims claims = JwtUtil.parse(token);
        logger.info("{}", claims);
    }

    @Test
    public void generateJwtTokenTest() throws Exception {
        // 获取私钥
        PrivateKey privateKey = RsaUtil.getPrivateKey(ResourceUtils.getFile(privateFilePath).getPath());
        // 私钥加密，生成jwt token
        String token = JwtUtil.generateJwtToken(getUserDetailsModel(), privateKey);
        logger.info("通过非对称加密算法生成的jwt token为：{}", token);
    }

    @Test
    public void parseJwtTokenTest() throws Exception {
        // 获取公钥
        PublicKey publicKey = RsaUtil.getPublicKey(ResourceUtils.getFile(publicFilePath).getPath());
        // 公钥解密，解析jwt token中的用户信息
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjp7InBob25lIjoiMTUyMTc3NjE2NTkiLCJpZCI6MSwidXNlck5hbWUiOiJhZG1pbiIsImVtYWlsIjoiMTIzNDU2QHFxLmNvbSIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX2FkbWluIn1dfSwianRpIjoiNGFjY2U0YWQtMWM5MC00MWMwLTk2MTUtNjBkY2NiZTk2M2I4Iiwic3ViIjoiYWRtaW4iLCJpYXQiOjE2NzEwMDI4MzQsImV4cCI6MTY3MTAxMDAzM30.FK74rZt4FfvytLaGXghnLVLGOkPGIvkLteMesDyNhn7mANoxRGJZpFUx3cCNulKOpEZb_BEHeUJCoM-EtybiuTxLaJi9Z79fi40SLZBscw2AlDME2hLmHmAk3EzeWJdGfoY93tAT3jVFoC4lj3JYU05Eo1JzwOCgp6Tqa_V4N_WyH8to4ITQTkP58qBQspK2zcfh4_SuOdrHNPxkZFKaxlaBAiJN8Jw1oyDEnEtOCSkSCVSdobjanIS_f_RGjpYA-Cu-FApO0_DhehuQLLZh15DMXkZGrwm4NoxQwDkxg-TyQ-5S4Vl72Q5GzRXGOgqAkQy8orxfIct9dggh2bHaZw";
        Claims claims = JwtUtil.parseJwtToken(token, publicKey);
        String subject = claims.getSubject();
        logger.info("用户名为：{}", subject);
        Object user = claims.get(Constants.JWT_PAYLOAD_USER_KEY);
        Map<String, Object> userMap = (Map<String, Object>) user;
        System.out.println(userMap);
        Map<String, Object> map = claims.get("user", Map.class);
        System.out.println(map);
    }
}
