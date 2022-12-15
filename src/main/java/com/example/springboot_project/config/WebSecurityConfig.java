package com.example.springboot_project.config;

import com.example.springboot_project.exception.ErrorEnum;
import com.example.springboot_project.exception.ServiceException;
import com.example.springboot_project.filter.TokenLoginFilter;
import com.example.springboot_project.filter.TokenParseFilter;
import com.example.springboot_project.model.UserDetailsModel;
import com.example.springboot_project.service.impl.UserDetailsServiceImpl;
import com.example.springboot_project.util.JwtUtil;
import com.example.springboot_project.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringSecurity配置类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/7 01:02
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() // 开启httpbasic认证，对应的springsecurity的过滤器链会启用BasicAuthenticationFilter过滤器

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/student/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin(); // 开启表单认证，对应的springsecurity的过滤器链会启用UsernamePasswordAuthenticationFilter过滤器
        http.addFilterBefore(tokenParseFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        // 使用token，禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    /***
     * PasswordEncoder密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenLoginFilter tokenLoginFilter() throws Exception {
        TokenLoginFilter tokenLoginFilter = new TokenLoginFilter();
        tokenLoginFilter.setAuthenticationManager(authenticationManagerBean());
        // 设置自定义登录成功处理器
        tokenLoginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        // 设置自定义登录失败处理器
        tokenLoginFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        return tokenLoginFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    @Bean
    public TokenParseFilter tokenParseFilter() {
        return new TokenParseFilter();
    }

    // 加@Bean注解会报错
    // @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsServiceImpl;
    }

    public static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        private HandlerExceptionResolver handlerExceptionResolver;

        /***
         * 自定义登录成功处理器
         */
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            // 认证成功，生成jwt
            UserDetailsModel userDetailsModel = (UserDetailsModel)authentication.getPrincipal();
            // 获取私钥
            PrivateKey privateKey = null;
            try {
                privateKey = RsaUtil.getPrivateKey(ResourceUtils.getFile("classpath:private.txt").getPath());
            } catch (Exception e) {
                handlerExceptionResolver.resolveException(request, response, null, new ServiceException(ErrorEnum.GET_PRIVATEKEY_ERROR));
                return;
            }
            String token = JwtUtil.generateJwtToken(userDetailsModel, privateKey);
            // token 设置到响应头
            response.setHeader("Authrozation", token);
            response.setContentType("application/json;charset=UTF-8");
            String msg = String.format("{\"code\": 200, \"success\": true, \"message\": \"登录成功\", \"data\": %s}", token);
            response.getWriter().write(msg);
        }
    }

    /***
     * 自定义登录失败处理器
     */
    public static class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            // 以返回JSON数据为例
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 400, \"success\": false, \"message\": \"用户名或密码错误\", \"data\": null}");
        }
    }
}
