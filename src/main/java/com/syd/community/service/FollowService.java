package com.syd.community.service;

import com.syd.community.entity.User;
import com.syd.community.util.CommunityConstant;
import com.syd.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    //关注（编程式事务处理）
    public void follow(int userId, int entityType, int enetityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, enetityId);

                operations.multi();
                // 某个用户关注的实体  followee:{userId}:{entityType} -> zset(entityId,now)
                // 添加到当前用户的关注列表
                operations.opsForZSet().add(followeeKey, enetityId, System.currentTimeMillis());
                // 某个实体拥有的粉丝  follower:{entityType}:{entityId} -> zset(userId,now)
                // 添加到对应实体(用户)的粉丝列表
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    //取消关注（编程式事务处理）
    public void unfollow(int userId, int entityType, int enetityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, enetityId);

                operations.multi();
                // 从当前用户的关注列表中删除
                operations.opsForZSet().remove(followeeKey, enetityId);
                // 从对应用户的粉丝列表中删除
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }

    // 查询关注的实体的数量
    public long findFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 查询实体的粉丝的数量
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 查询当前用户是否已关注该实体
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    // 查询某用户关注的人
    public List<Map<String, Object>> findFollowees(int userId, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        //此处的limit和MySQL中的不太一样，此处为截止的索引，所以为offset + limit - 1;
        //redis的 reverseRange 和 range方法都是返回一个自己实现set接口的的有序集合。
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }

    // 查询某用户的粉丝
    public List<Map<String, Object>> findFollowers(int userId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);

        if (targetIds == null) {
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Integer targetId : targetIds) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            list.add(map);
        }

        return list;
    }


}
