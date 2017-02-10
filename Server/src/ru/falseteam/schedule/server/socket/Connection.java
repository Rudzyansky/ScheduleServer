package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.PermissionManager;
import ru.falseteam.vframe.socket.SocketWorker;

import java.net.Socket;

public class Connection extends ConnectionAbstract<Groups> {
    private static final AccessManager ACCESS_MANAGER = new AccessManager();

    private User user = User.Factory.getDefault();

    public Connection(Socket socket, SocketWorker worker) {
        super(socket, worker);
    }

    @Override
    protected PermissionManager<Groups> getPermissionManager() {
        return ACCESS_MANAGER;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        permission = user.permissions;
        this.user = user;
    }
}
