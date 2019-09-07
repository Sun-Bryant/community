package com.syd.community.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.jws.Oneway;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommunityUtil.class);

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5加密
    //hello -->  abc123def456
    //hello + salt(3a4e8) -->  abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", code);
//        map.put("msg", msg);
//        String json=JSON.toJSONString(map);
//        return json;
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", code);
//        String json=JSON.toJSONString(map);
//        return json;
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 22);
        System.out.println(getJSONString(0, "ok", map));
    }
}
