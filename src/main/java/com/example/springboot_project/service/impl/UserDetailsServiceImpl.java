package com.example.springboot_project.service.impl;

import com.example.springboot_project.model.UserDetailsModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 根据用户名查询用户详细信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 目前用户信息写死，之后修改为从数据库查询
        if ("admin".equals(username)) {
            UserDetailsModel userDetailsModel = new UserDetailsModel();
            userDetailsModel.setId(1);
            userDetailsModel.setUserName("admin");
            userDetailsModel.setPassword("12345678");
            userDetailsModel.setPhone("15217761659");
            userDetailsModel.setEmail("123456@qq.com");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
            Collection<GrantedAuthority> collection = new HashSet<>();
            collection.add(simpleGrantedAuthority);
            userDetailsModel.setAuthorities(collection);
            return userDetailsModel;
        } else {
            throw new UsernameNotFoundException("not found username: " + username);
        }
    }
}
