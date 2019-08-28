package com.syd.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary  //具有更高的优先级
public class AlphaDaoMyBatisImpl implements AlphaDao {

    @Override
    public String select() {
        return "Mybatis";
    }

}
