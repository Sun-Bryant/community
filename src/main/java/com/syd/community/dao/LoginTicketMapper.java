package com.syd.community.dao;

import com.syd.community.entity.LoginTicket;
import com.syd.community.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated
public interface LoginTicketMapper {

    @Insert({
            " insert into login_ticket(user_id, ticket, status, expired) ",
            " values(#{userId}, #{ticket}, #{status}, #{expired}) "
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            " select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            " update login_ticket set status = #{status} where ticket = #{ticket} "
    })
    int updateStatus(String ticket, int status);

//    @Update({
//            "<script>",
//            "update login_ticket set status=#{status} where ticket=#{ticket} ",
//            "<if test=\"ticket!=null\"> ",
//            "and 1=1 ",
//            "</if>",
//            "</script>"
//    })
//    int updateStatus(String ticket, int status);

}
