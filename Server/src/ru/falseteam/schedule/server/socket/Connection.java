package ru.falseteam.schedule.server.socket;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.socket.commands.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Connection implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;

    private User user = User.Factory.getDefault();
    private long uptime = System.currentTimeMillis();

    Connection(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void send(Map<String, Object> map) {
        try {
            // Эта строчка появилась здесь после двух часов мучений
            out.reset();
            // ----------------------------------------------------
            out.writeObject(map);
            out.flush();
        } catch (IOException ignore) {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            Worker.removeFromList(this);
            Console.print("Client " + socket.getInetAddress().getHostAddress() + " disconnected");
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
            Console.print("Client " + socket.getInetAddress().getHostAddress() + " connected");
            while (true) {
                Object o = in.readObject();
                if (!(o instanceof Map)) throw new MyException("not Map");
                Map<String, Object> map = (Map<String, Object>) o;
                if (!map.containsKey("command")) throw new MyException("command not found");
                CommandWorker.exec(this, map);
            }
        } catch (MyException e) {
            Console.err("Client " + socket.getInetAddress().getHostAddress()
                    + " disconnected // Reason: " + e.getMessage());
        } catch (IOException | ClassNotFoundException ignore) {
        }
        disconnect();
    }

    public long getUptime() {
        return uptime;
    }

    public String getName() {
        return socket.getInetAddress().getHostAddress();
    }
}
