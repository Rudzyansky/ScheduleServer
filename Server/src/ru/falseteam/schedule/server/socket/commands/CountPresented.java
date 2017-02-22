package ru.falseteam.schedule.server.socket.commands;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.serializable.UserPresented;
import ru.falseteam.schedule.server.sql.JournalInfo;
import ru.falseteam.schedule.server.sql.UserInfo;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountPresented extends ProtocolAbstract {

    @Override
    public void exec(Map<String, Object> map, ConnectionAbstract connection) {
        Container c = new Container(getName(), true);
        List<JournalRecord> currentWeek = JournalInfo.getWeek();
        int count = 0;
        for (JournalRecord record : currentWeek)
            if (!record.lesson.audience.equals("с/з")) ++count;
        c.data.put("count", count);
        List<UserPresented> users = new ArrayList<>();
        for (User u : UserInfo.getUsers()) {
            if (u.permissions.equals(Groups.unconfirmed)) continue;
            UserPresented up = UserPresented.Factory.getFromUser(u);
            for (JournalRecord record : currentWeek) {
                if (record.lesson.audience.equals("с/з")) continue;
                if (record.presented.get(up.user.atList)) ++up.presented;
                else ++up.notPresented;
            }
            users.add(up);
        }
        c.data.put("users", users);
        connection.send(c);
    }
}
