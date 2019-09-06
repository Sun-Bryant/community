package com.syd.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
    /**
     * 这个注解起到标识的作用，打上这个注解，你必须登录可以访问，不打这个标记，随便访问。
     */
}
