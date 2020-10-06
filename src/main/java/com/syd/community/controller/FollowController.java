package com.syd.community.controller;

import com.syd.community.entity.Event;
import com.syd.community.entity.Page;
import com.syd.community.entity.User;
import com.syd.community.event.EventProducer;
import com.syd.community.service.FollowService;
import com.syd.community.service.UserService;
import com.syd.community.util.CommunityConstant;
import com.syd.community.util.CommunityUtil;
import com.syd.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    // 关注某人
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        //为了防止未登录   需要用拦截器对这个方法做一个检查
        User user = hostHolder.getUser();

        // 关注
        followService.follow(user.getId(), entityType, entityId);

        // 触发关注事件（目前只能关注人）
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        //发送消息
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "已关注!");
    }

    // 取消关注某人
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        //为了防止未登录   需要用拦截器对这个方法做一个检查
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注!");
    }

    //查询关注列表
    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
        // 获得对应Id的用户（注意这里不能用hostHolder，因为可能是查看别人的关注列表，并不是当前用户的）
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (userList != null) { //当前用户对该关注列表里的所有用户的关注状态
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/followee";
    }

    //获取粉丝列表
    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        // 获得对应Id的用户（注意这里不能用hostHolder，因为可能是查看别人的粉丝列表，并不是当前用户的）
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        // 获取粉丝列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) { //当前用户对该粉丝列表的所有用户的关注状态
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }

        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }

}
