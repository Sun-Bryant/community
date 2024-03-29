package com.syd.community.service;

import com.syd.community.dao.AlphaDao;
import com.syd.community.dao.DiscussPostMapper;
import com.syd.community.dao.UserMapper;
import com.syd.community.entity.DiscussPost;
import com.syd.community.entity.User;
import com.syd.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype") //这是正常的原型模式   默认是singleton单例模式
 public class AlphaService {

    private static final Logger logger = LoggerFactory.getLogger(AlphaService.class);

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String find() {
        return alphaDao.select();
    }

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct  //这个注解的作用：这个方法会在构造器之后调用
    public void init() {
        System.out.println("初始化AlphaService");

    }

    @PreDestroy  //在销毁对象之前调用
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    // 3种常见的传播机制。一共有七种。
    // 1)REQUIRED: 支持当前事务/外部事务(A调B,对于B来说，A就是当前事务),如果当前事物不存在，则创建新事务.
    //      A调B,如果A有事务，就按A的来，否则，就创建一个新事务，按自己的来。
    // 2)REQUIRES_NEW: 无论如何都创建一个新事务,如果有当前事务，就暂停(挂起)当前事务.
    //      A调B,B不管A的事务，有也给你暂停掉。我永远都创建一个新事务，按照自己的事务来执行。
    // 3)NESTED: 嵌套事务，如果当前事务存在，那么嵌套在该事务中执行(独立的提交和回滚 相当于子事务)。如果当前事务不存在，则表现跟REQUIRED一样。
    //      A调B，A有事务，那我就嵌套在A的事物里执行(B是有独立的提交和回滚)。
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "ok";
    }

    //编程式事务
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://images.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");

                return "ok";
            }
        });
    }

    // 让该方法在多线程环境下,被异步的调用.（启动一个线程来调用这个方法，这个方法和主线程是并发执行的）
    @Async
    public void execute1() {
        logger.debug("execute1");
    }

   /* @Scheduled(initialDelay = 10000, fixedRate = 1000)*/
    public void execute2() {
        logger.debug("execute2");
    }


}
