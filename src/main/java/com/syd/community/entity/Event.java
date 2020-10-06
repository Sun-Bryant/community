package com.syd.community.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {

    private String topic; // 事件主题
    private int userId;  // 事件的触发者
    private int entityType;  // 事件触发在哪个实体之上
    private int entityId;
    private int entityUserId;   // 实体的作者是谁
    private Map<String, Object> data = new HashMap<>();  // 其他额外的数据

    public String getTopic() {
        return topic;
    }

    // set方法的返回值是Event。这样的好处是我们set一个值后，返回当前对象，还可以接着set其他值。
    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
