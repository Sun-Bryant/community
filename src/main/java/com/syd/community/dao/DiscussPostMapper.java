package com.syd.community.dao;

import com.syd.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

//    List<DiscussPost> selectDiscussPosts(@Param("userId")int userId, @Param("offset")int offset, @Param("limit")int limit);
    List<DiscussPost> selectDiscussPosts(int userId, int offset,int limit);

    /**
     * @Param注解 用于给参数起别名。
     *如果需要动态的拼接一个条件，并且这个方法有且只有这一个条件（在<if>里使用），这时候参数之前必须采用@Param起别名。
     *查询有多少条数据
     */
    int selectDiscussPostRows(@Param("userId")int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
