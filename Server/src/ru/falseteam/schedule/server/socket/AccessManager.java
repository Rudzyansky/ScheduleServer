package ru.falseteam.schedule.server.socket;


import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.server.socket.commands.*;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.PermissionManager;
import ru.falseteam.vframe.socket.SubscriptionProtocol;

class AccessManager extends PermissionManager<Groups> {
    public AccessManager() {
        super(Groups.class, Groups.guest);
        addCommand(new Auth(), Groups.guest);
        addCommand(new GetLessons(), Groups.developer, Groups.admin, Groups.user, Groups.unconfirmed);
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

        addCommand(new SubscriptionProtocol(), Groups.values());

        setDefaultProtocol((container, connection) -> {
            Container c = new Container("AccessDenied", true);
            c.data.put("command", container.protocol);
            connection.send(c);
        });
    }
}