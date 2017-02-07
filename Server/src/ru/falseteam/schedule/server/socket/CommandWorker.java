package ru.falseteam.schedule.server.socket;


import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.server.socket.commands.*;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.HashMap;
import java.util.Map;

class CommandWorker {
    private static Map<Groups, Map<String, ProtocolAbstract>> permissions;

    static {
        permissions = new HashMap<>();

        permissions.put(Groups.guest, new HashMap<>());
        permissions.put(Groups.unconfirmed, new HashMap<>());
        permissions.put(Groups.user, new HashMap<>());
        permissions.put(Groups.admin, new HashMap<>());
        permissions.put(Groups.developer, new HashMap<>());

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

        addCommand(new GetJournal(), Groups.developer, Groups.admin);
        addCommand(new UpdateJournalRecord(), Groups.developer, Groups.admin);
    }

    private static void addCommand(ProtocolAbstract c, Groups... groupies) {
        for (Groups g : groupies) permissions.get(g).put(c.getName(), c);
    }

    private final static ProtocolAbstract accessDenied = new AccessDenied();

    public static ProtocolAbstract getAccesDenied() {
        return accessDenied;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    static Map<String, ProtocolAbstract> get(Groups g) {
        return permissions.get(g);
    }
}
