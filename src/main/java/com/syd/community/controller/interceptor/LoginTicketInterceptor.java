package com.syd.community.controller.interceptor;

import com.syd.community.entity.LoginTicket;
import com.syd.community.entity.User;
import com.syd.community.service.UserService;
import com.syd.community.util.CookieUtil;
import com.syd.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //controller请求之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //从cookie获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
//            检查凭证是否有效
            if (ticket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户（暂存用户）  如果仅仅把用户存在容器中，在多线程并发情况下可能会产生冲突，所有我们需要使用hostHolder
                hostHolder.setUser(user);
                // 构建用户认证的结果（凭证）,并存入SecurityContext,以便于Security进行授权.
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

            }
        }
        return true;
    }

    //controller请求之后 模板引擎之前 执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        //将用户信息添加到ModelAndView
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    // 在模板引擎（TemplateEngine）之后执行。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //在模板引擎返回之后清理数据
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
