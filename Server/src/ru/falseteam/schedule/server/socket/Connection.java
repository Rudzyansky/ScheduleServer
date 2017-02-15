package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

import java.net.Socket;

public class Connection extends ConnectionAbstract<Groups> {
    private User user = User.Factory.getDefault();

    public Connection(Socket socket, SocketWorker<Groups> worker) {
        super(socket, worker);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        //noinspection unchecked
        setPermission(user.permissions);
        this.user = user;
    }
}
