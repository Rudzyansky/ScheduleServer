package ru.falseteam.schedule.server.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ConnectionAbstract;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

import java.net.Socket;
import java.util.Map;

public class Connection extends ConnectionAbstract {
    private final Logger log = LogManager.getLogger();


    private User user = User.Factory.getDefault();
    private final long uptime = System.currentTimeMillis();
    private long lastPing = System.currentTimeMillis();

    public Connection(Socket socket, SocketWorker worker) {
        super(socket, worker);
    }


    public long getUptime() {
        return uptime;
    }

    long getLastPing() {
        return lastPing;
    }

    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected Map<String, ProtocolAbstract> getProtocols() {
        return CommandWorker.get(user.permissions);
    }
}
