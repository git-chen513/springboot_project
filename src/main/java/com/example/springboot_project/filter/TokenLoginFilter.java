package com.example.springboot_project.filter;

import com.example.springboot_project.model.UserDetailsModel;
import com.example.springboot_project.util.JwtUtil;
import com.example.springboot_project.util.RsaUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ResourceUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;

/**
 * todo {这里必须加注释}
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/11 14:24
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = obtainUsername(request);
//        username = (username != null) ? username.trim() : "";
//        String password = obtainPassword(request);
//        password = (password != null) ? password : "";
//        UserDetails userDetails = null;
//        if (username.equals("admin") && password.equals("123456")) {
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
//            Collection collection = new ArrayList();
//            collection.add(simpleGrantedAuthority);
//            userDetails = new User(username, password, true, true, true, true, collection);
//        } else {
//            throw new BadCredentialsException("认证失败");
//        }
//        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
//        return authentication;
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 认证成功，生成jwt token
        UserDetailsModel userDetailsModel = (UserDetailsModel)authResult.getPrincipal();
        // 获取私钥
        PrivateKey privateKey = null;
        try {
            privateKey = RsaUtil.getPrivateKey(ResourceUtils.getFile("classpath:private.txt").getPath());
        } catch (Exception e) {
            logger.error("获取私钥异常, {}", e);
            throw new ServletException("获取私钥异常");
        }
        String token = JwtUtil.generateJwtToken(userDetailsModel, privateKey);
        response.setHeader("Authrozation", token);
        response.getWriter().write(token);
        // 把认证成功的用户信息写入spring security上下文中
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.getWriter().write("error username or error password");
//    }
}
