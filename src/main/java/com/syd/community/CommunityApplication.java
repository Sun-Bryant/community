package com.syd.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 *
 * 项目启动以CommunityApplication类为入口，其实CommunityApplication类是一个配置类
 *
 */
@SpringBootApplication
public class CommunityApplication {

	@PostConstruct
	public void init() {
		// 解决netty启动冲突问题
		// see Netty4Utils.setAvailableProcessors()
		System.setProperty("es.set.netty.runtime.available.processors", "false");
	}

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}
}
