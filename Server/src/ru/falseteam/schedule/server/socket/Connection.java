package ru.falseteam.schedule.server.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.falseteam.schedule.serializable.User;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Connection implements Runnable {
    private final Logger log = LogManager.getLogger();

    private final SSLSocket socket;
    private ObjectOutputStream out;

    private User user = User.Factory.getDefault();
    private final long uptime = System.currentTimeMillis();
    private long lastPing = System.currentTimeMillis();

    private boolean connected = true;

    Connection(SSLSocket socket) {
        this.socket = socket;
        new Thread(this, "Connection with " + socket.getInetAddress().getHostAddress()).start();
    }

    public void send(Map<String, Object> map) {
        try {
            out.reset();
            out.writeObject(map);
            out.flush();
        } catch (IOException ignore) {
            disconnect();
        }
    }

    public synchronized void disconnect() {
        if (!connected) return;
        connected = false;
        try {
            Worker.removeFromList(this);
            log.trace("Client {} disconnected", socket.getInetAddress().getHostAddress());
            socket.close();
        } catch (IOException ignore) {
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "unchecked"})
    @Override
    public void run() {
        class MyException extends Exception {
            private MyException(String message) {
                super(message);
            }
        }
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            log.trace("Client {} connected", socket.getInetAddress().getHostAddress());

            while (true) {
                Object o = in.readObject();
                if (!(o instanceof Map)) throw new MyException("not Map");
                Map<String, Object> map = (Map<String, Object>) o;
                if (!map.containsKey("command")) throw new MyException("command not found");
                CommandWorker.exec(this, map);
            }
        } catch (MyException | ClassNotFoundException e) {
            log.error("Client {} disconnected // Reason: {}",
                    socket.getInetAddress().getHostAddress(), e.getMessage());
            e.printStackTrace();
        } catch (IOException ignore) {
        }
        disconnect();
    }

    public long getUptime() {
        return uptime;
    }

    public String getName() {
        return socket.getInetAddress().getHostAddress();
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
}
