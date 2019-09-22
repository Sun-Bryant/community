package com.syd.community.controller.interceptor;

import com.syd.community.entity.User;
import com.syd.community.service.DataService;
import com.syd.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    // controller请求之前执行
    // 每次请求都记录，存入redis中，方便统计。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 统计UV （IP）
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        // 统计DAU (ID)
        User user = hostHolder.getUser();
        if (user != null) {
            dataService.recordDAU(user.getId());
        }

        return true;
    }
}
