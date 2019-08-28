package com.syd.community.service;

import com.syd.community.dao.DiscussPostMapper;
import com.syd.community.dao.UserMapper;
import com.syd.community.entity.DiscussPost;
import com.syd.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

}
