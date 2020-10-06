package com.syd.community.controller;

import com.syd.community.entity.DiscussPost;
import com.syd.community.entity.Page;
import com.syd.community.entity.User;
import com.syd.community.service.DiscussPostService;
import com.syd.community.service.LikeService;
import com.syd.community.service.UserService;
import com.syd.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String root() {
        return "forward:/index";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode", defaultValue = "0") int orderMode) {
        //方法调用前，SpringMVC（DispatcherServlet）会自动实例化Model和Page，并将Page注入Model。
        //所以，在Thymeleaf中可以直接访问Page中的对象
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);

        // userId = 0,查询所有用户的帖子数据
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(), orderMode);
        // discussPosts整合 帖子post 和 user ，相当于vo。
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);
        return "index";
    }

    // 获取错误页面
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    // 拒绝访问时的提示页面（或者权限不足）
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }

}
