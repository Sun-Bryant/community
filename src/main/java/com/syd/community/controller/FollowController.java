package com.syd.community.controller;

import com.syd.community.entity.User;
import com.syd.community.service.FollowService;
import com.syd.community.util.CommunityUtil;
import com.syd.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        //为了防止未登录   需要用拦截器对这个方法做一个检查
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已关注!");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        //为了防止未登录   需要用拦截器对这个方法做一个检查
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注!");
    }

}
