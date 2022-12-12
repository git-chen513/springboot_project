package com.example.springboot_project.config;

import com.example.springboot_project.filter.TokenLoginFilter;
import com.example.springboot_project.filter.TokenParseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/7 01:02
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123456"))
//                .and()
//                .passwordEncoder(passwordEncoder());
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String passwd = passwordEncoder.encode("123456");
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("123456")).roles("admin");
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
}
