package com.syd.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 项目启动以CommunityApplication类为入口，其实CommunityApplication类是一个配置类
 *
 * 首先我们知道 Spring Boot 启动需要有一个启动引导类，这个类除了是应用的入口之外，
 * 还发挥着配置的 Spring Boot 的重要作用。
 *
 * @SpringBootApplication的注解，我们点击进去可以发现，这个注解发挥着多个注解的作用：
 * 这里简要的说下@SpringBootConfiguration和@ComponentScan注解。
 * 前者实质为@Configuration注解，这个注解相比大家都接触过，也就是起到声明这个类为配置类的作用，
 * 而后者起到开启自动扫描组件的作用。会扫描配置类所在的包以及子包下的bean。
 * @EnableAutoConfiguration这个注解，这个注解的作用是开启 Spring Boot 的自动配置功能
 *
 * 总结一下 Spring Boot 的自动配置：Spring Boot 启动的时候，会扫描ClassPath下的所有 jar 包，
 * 将其spring.factories文件中key为EnableAutoConfiguration的所有值取出，这些值其实是类的全限定名，也就是自动配置类的全限定名，
 * 然后 Spring Boot 通过这些全限定名进行类加载(反射)，将这些自动配置类添加到 Spring 容器中。
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
