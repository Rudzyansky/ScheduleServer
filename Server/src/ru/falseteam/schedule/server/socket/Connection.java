package ru.falseteam.schedule.server.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ServerConnectionAbstract;
import ru.falseteam.vframe.socket.ServerProtocolAbstract;
import ru.falseteam.vframe.socket.ServerSocketWorker;

import java.net.Socket;
import java.util.Map;

public class Connection extends ServerConnectionAbstract {
    private final Logger log = LogManager.getLogger();


    private User user = User.Factory.getDefault();
    private final long uptime = System.currentTimeMillis();
    private long lastPing = System.currentTimeMillis();

    public Connection(Socket socket, ServerSocketWorker worker) {
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
    protected Map<String, ServerProtocolAbstract> getProtocols() {
        return CommandWorker.get(user.permissions);
    }
}
