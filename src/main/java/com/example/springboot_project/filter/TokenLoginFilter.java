package com.example.springboot_project.filter;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 自定义token登录过滤器，认证成功后，生成jwt，并且将认证成功的用户信息写入spring security上下文
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/11 14:24
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
}
