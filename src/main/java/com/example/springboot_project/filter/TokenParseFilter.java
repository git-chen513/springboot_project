package com.example.springboot_project.filter;

import com.example.springboot_project.constants.Constants;
import com.example.springboot_project.exception.ServiceException;
import com.example.springboot_project.model.UserDetailsModel;
import com.example.springboot_project.util.JwtUtil;
import com.example.springboot_project.util.RsaUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * 自定义过滤器：如果请求头携带了token，解析token，获取到用户信息保存到spring security上下文中
 */
public class TokenParseFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authrozation");
        if (token != null) {
            // 获取公钥
            PublicKey publicKey = null;
            try {
                publicKey = RsaUtil.getPublicKey(ResourceUtils.getFile("classpath:public.txt").getPath());
            } catch (Exception e) {
                logger.error("获取公钥异常：", e);
                throw new ServletException("获取公钥异常");
            }
            Claims claims = null;
            try {
                claims = JwtUtil.parseJwtToken(token, publicKey);
            } catch (ServiceException e) {
                handlerExceptionResolver.resolveException(request, response, null, e);
                return;
            }
            Object user = claims.get(Constants.JWT_PAYLOAD_USER_KEY);
            Map<String, Object> userMap = (Map<String, Object>) user;
            UserDetailsModel userDetailsModel = new UserDetailsModel();
            userDetailsModel.setId((Integer) userMap.get("id"));
            userDetailsModel.setUserName((String) userMap.get("userName"));
            userDetailsModel.setPhone((String) userMap.get("phone"));
            userDetailsModel.setEmail((String) userMap.get("email"));

            // TODO 这里的用户权限信息应该是从token解析出来，由于这里强转有问题，暂时写死，后续再进行完善
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_admin");
            Collection<GrantedAuthority> collection = new HashSet<>();
            collection.add(simpleGrantedAuthority);
            userDetailsModel.setAuthorities(collection);

            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
                    .authenticated(userDetailsModel, null, userDetailsModel.getAuthorities());
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
