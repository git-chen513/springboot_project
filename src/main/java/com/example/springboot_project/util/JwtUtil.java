package com.example.springboot_project.util;

import com.alibaba.druid.util.StringUtils;
import com.example.springboot_project.constants.Constants;
import com.example.springboot_project.exception.ErrorEnum;
import com.example.springboot_project.exception.ServiceException;
import com.example.springboot_project.model.UserDetailsModel;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 对称加密使用一个任意字符串作为秘钥，加密和解密都使用同一个秘钥
     * 这个秘钥是防止JWT被篡改的关键，不能泄露
     */
    private final static String secretKey = "whatever";
    /**
     * token默认过期时间目前设置成2小时，这个配置随业务需求而定
     */
    private final static Duration expiration = Duration.ofHours(2);

    /**
     * 对称加密方式生成JWT
     * @param userDetailsModel 用户信息
     * @return JWT
     */
    public static String generate(UserDetailsModel userDetailsModel) {
        // 设置令牌过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        Map<String, Object> map = new HashMap<>();
        map.put("id", userDetailsModel.getId());
        map.put("userName", userDetailsModel.getUsername());
        map.put("phone", userDetailsModel.getPhone());
        map.put("email", userDetailsModel.getEmail());
        map.put("authorities", userDetailsModel.getAuthorities());

        return Jwts.builder()
                .claim(Constants.JWT_PAYLOAD_USER_KEY, map) // 将用户信息放进JWT
                .setId(UUID.randomUUID().toString()) // 设置jti
                .setSubject(userDetailsModel.getUsername()) // 设置令牌主题为用户名
                .setIssuedAt(new Date()) // 设置JWT签发时间
                .setExpiration(expiryDate)  // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, secretKey) // 设置加密算法和秘钥
                .compact();
    }

    /**
     * 对称加密方式解析JWT
     * @param token JWT字符串
     * @return 解析成功返回Claims对象，解析失败返回null
     */
    public static Claims parse(String token) {
        // 如果是空字符串直接返回null
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        // 这个Claims对象包含了许多属性，比如签发时间、过期时间以及存放的数据等
        Claims claims = null;
        // 解析失败了会抛出异常，token过期、token非法都会导致解析失败
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey) // 设置秘钥
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("token 已过期，请重新登录！");
            throw e;
        } catch (JwtException e) {
            logger.error("token 解析失败");
            throw e;
        }
        return claims;
    }


    /**
     * 非对称加密方式-私钥加密，生成jwt token
     * @param userDetailsModel 用户信息
     * @param privateKey 私钥
     * @return
     */
    public static String generateJwtToken(UserDetailsModel userDetailsModel, PrivateKey privateKey) {
        // 设置令牌过期时间
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        Map<String, Object> map = new HashMap<>();
        map.put("id", userDetailsModel.getId());
        map.put("userName", userDetailsModel.getUsername());
        map.put("phone", userDetailsModel.getPhone());
        map.put("email", userDetailsModel.getEmail());
        map.put("authorities", userDetailsModel.getAuthorities());

        return Jwts.builder()
                .claim(Constants.JWT_PAYLOAD_USER_KEY, map) // 将用户信息放进JWT
                .setId(UUID.randomUUID().toString()) // 设置jti
                .setSubject(userDetailsModel.getUsername()) // 设置令牌主题为用户名
                .setIssuedAt(new Date()) // 设置JWT签发时间
                .setExpiration(expiryDate)  // 设置过期时间
                .signWith(SignatureAlgorithm.RS256, privateKey) // 使用RSA非对称加密算法、指定秘钥
                .compact();
    }

    /**
     * 非对称加密方式-公钥解密，解析jwt token
     * @param token
     * @param publicKey 公钥
     * @return
     */
    public static Claims parseJwtToken(String token, PublicKey publicKey) {
        // 如果是空字符串直接返回null
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        // 这个Claims对象包含了许多属性，比如签发时间、过期时间以及存放的数据等
        Claims claims = null;
        // 解析失败了会抛出异常，token过期、token非法都会导致解析失败
        try {
            claims = Jwts.parser()
                    .setSigningKey(publicKey) // 设置公钥
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("token 已过期", e);
            throw new ServiceException(ErrorEnum.EXPIRED_TOKEN);
        } catch (JwtException e) {
            logger.error("token 解析失败", e);
            throw new ServiceException(ErrorEnum.ILLEGAL_TOKEN);
        }
        return claims;
    }
}
