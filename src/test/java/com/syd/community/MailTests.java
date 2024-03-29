package com.syd.community;


import com.syd.community.util.CommunityUtil;
import com.syd.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)//在测试代码中也以CommunityApplication类为配置文件
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("1091245120@qq.com", "TEST", "welcome");

    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "syd");

        String content = templateEngine.process("/mail/demo", context);
        System.out.printf(content);

        mailClient.sendMail("1091245120@qq.com", "HTMl", content);
    }
    @Test
    public void testCommunityUtil() {
        System.out.println(CommunityUtil.getJSONString(1, "邮箱不正确"));
    }

}
