package ru.falseteam.schedule.server.sql;

import ru.falseteam.schedule.server.socket.Connection;

public class UserInfo {

    private boolean exists;
    private String name;
    private String vk_token;
    private int vk_id;
    private Connection.Groups group;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVk_token() {
        return vk_token;
    }

    public void setVk_token(String vk_token) {
        this.vk_token = vk_token;
    }

    public int getVk_id() {
        return vk_id;
    }

    public void setVk_id(int vk_id) {
        this.vk_id = vk_id;
    }

    public Connection.Groups getGroup() {
        return group;
    }

    public void setGroup(Connection.Groups group) {
        this.group = group;
    }
}
