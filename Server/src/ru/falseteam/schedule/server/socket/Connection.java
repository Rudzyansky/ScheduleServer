package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

import java.net.Socket;

public class Connection<T extends Enum<T>> extends ConnectionAbstract<T> {
    private User user = User.Factory.getDefault();

    public Connection(Socket socket, SocketWorker<T> worker) {
        super(socket, worker);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        //noinspection unchecked
        permission = (T) user.permissions;
        this.user = user;
    }
}
