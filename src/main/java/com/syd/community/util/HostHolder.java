package com.syd.community.util;

import com.syd.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 起一个容器的作用
 * 持有用户信息，用户代替session对象（多线程  线程隔离的）
 */
@Component
public class HostHolder {

    /**
     * ThreadLocal实现线程隔离：
     * 存：先获取当前线程，然后通过这个线程获取一个Map对象,通过这个Map来存储信息。每个线程的Map对象不一样。
     * 取：先获取当前线程，再取到当前线程所对应的Map,从Map中取值。
     * 清理：获取当前线程的Map,然后把里面的清理掉
     * 以线程为key，存储池的。
     */
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }


}
