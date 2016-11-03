package ru.falseteam.schedule.serializable;

import java.io.Serializable;

/**
 * Structure contains all information about user.
 *
 * @author Evgeny Rudzyansky
 * @version 1.0
 */
public class User implements Serializable {
    public boolean exists;
    public int id;
    public String name;
    public String vkToken;
    public int vkId;
    public Groups group;

    // Empty private constructor.
    private User() {
    }

    public static class Factory {
        public static User getDefault() {
            User user = new User();
            user.exists = false;
            user.group = Groups.guest;
            return user;
        }
    }
}
