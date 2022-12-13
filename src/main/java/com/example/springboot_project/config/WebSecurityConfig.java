package com.example.springboot_project.config;

import com.example.springboot_project.filter.TokenLoginFilter;
import com.example.springboot_project.filter.TokenParseFilter;
import com.example.springboot_project.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                //.formLogin() // formLogin方式需要关闭csrf
//                .loginPage("login.html")
//                .loginProcessingUrl("/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/")

            //.and()
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

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.inMemoryAuthentication()
////                .withUser("admin")
////                .password(passwordEncoder().encode("123456"))
////                .and()
////                .passwordEncoder(passwordEncoder());
////        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////        String passwd = passwordEncoder.encode("123456");
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123456")).roles("admin");
//    }

    /***
     * PasswordEncoder密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TokenLoginFilter tokenLoginFilter() throws Exception {
        TokenLoginFilter tokenLoginFilter = new TokenLoginFilter();
        tokenLoginFilter.setAuthenticationManager(authenticationManagerBean());
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

    public static class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            //以返回JSON数据为例
            response.setContentType("application/json;charset=UTF-8");
            Map<String, String> map = new HashMap<>();
            map.put("code", "401");
            map.put("msg", "用户名或密码错误");
            response.getWriter().write(map.toString());
        }
    }

    // 加@Bean注解会报错
    // @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsServiceImpl;
    }
}
