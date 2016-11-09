package ru.falseteam.schedule.server.socket;


import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.server.socket.commands.*;

import java.util.HashMap;
import java.util.Map;

class CommandWorker {
    private static Map<Groups, Map<String, CommandInterface>> permissions;

    static {
        permissions = new HashMap<>();

        permissions.put(Groups.guest, new HashMap<>());
        permissions.put(Groups.unconfirmed, new HashMap<>());
        permissions.put(Groups.user, new HashMap<>());
        permissions.put(Groups.admin, new HashMap<>());
        permissions.put(Groups.developer, new HashMap<>());

        addCommand(new AccessDenied(), Groups.values());
        addCommand(new Ping(), Groups.values());
        addCommand(new Auth(), Groups.guest);
        addCommand(new GetLessons(), Groups.developer, Groups.admin, Groups.user);
        addCommand(new UpdateLesson(), Groups.developer, Groups.admin);
        addCommand(new DeleteLesson(), Groups.developer, Groups.admin);
        addCommand(new GetUsers(), Groups.developer, Groups.admin, Groups.user);
        // опасная зона
        addCommand(new UpdateUser(), Groups.developer, Groups.admin);
        addCommand(new DeleteUser(), Groups.developer, Groups.admin);
        // ------------
        addCommand(new GetTemplates(), Groups.developer, Groups.admin, Groups.user);
        addCommand(new GetWeekDays(), Groups.developer, Groups.admin, Groups.user);
        addCommand(new GetLessonNumbers(), Groups.developer, Groups.admin, Groups.user);

        addCommand(new UpdateTemplate(), Groups.developer, Groups.admin);
        addCommand(new DeleteTemplate(), Groups.developer, Groups.admin);
    }

    private static void addCommand(CommandInterface c, Groups... groupies) {
        for (Groups g : groupies) permissions.get(g).put(c.getName(), c);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public static void exec(Connection c, Map<String, Object> map) {
        Map<String, CommandInterface> currentPermissions = permissions.get(c.getUser().group);
        if (!currentPermissions.containsKey(map.get("command")))
            currentPermissions.get("forbidden").exec(c, map);
        else
            currentPermissions.get(map.get("command")).exec(c, map);
    }
}
