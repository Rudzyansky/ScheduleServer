package ru.falseteam.schedule.server.socket;

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

    private long uptime = System.currentTimeMillis();

    private static Map<Groups, Map<String, CommandInterface>> permissions;

    static {
        permissions = new HashMap<>();

        permissions.put(Groups.guest, new HashMap<>());
        permissions.put(Groups.unconfirmed, new HashMap<>());
        permissions.put(Groups.user, new HashMap<>());
        permissions.put(Groups.admin, new HashMap<>());
        permissions.put(Groups.developer, new HashMap<>());

        addCommand(new AccessDenied(), Groups.developer, Groups.admin, Groups.user, Groups.unconfirmed, Groups.guest);
        addCommand(new Auth(), Groups.developer, Groups.guest);
        addCommand(new GetPairs(), Groups.developer, Groups.admin, Groups.user);
        addCommand(new ChangePair(), Groups.developer, Groups.admin);
        addCommand(new DeletePair(), Groups.developer, Groups.admin);
    }

    private static void addCommand(CommandInterface c, Groups... groupies) {
        for (Groups g : groupies) permissions.get(g).put(c.getName(), c);
    }

    public enum Groups {
        guest,
        unconfirmed,
        user,
        admin,
        developer
    }

    public Groups currentGroup = Groups.guest;

    Connection(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    public void send(Map<String, Object> map) {
        try {
            out.writeObject(map);
            out.flush();
        } catch (IOException ignore) {
            disconnect();
        }
    }

    private void disconnect() {
        try {
            Worker.removeFromList(this);
            Console.print("Client " + socket.getInetAddress().getHostAddress() + " disconnected");
            socket.close();
        } catch (IOException ignore) {
        }
    }

    @SuppressWarnings({"SuspiciousMethodCalls", "InfiniteLoopStatement", "unchecked"})
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
                Map<String, CommandInterface> currentPermissions = permissions.get(currentGroup);
                if (!currentPermissions.containsKey(map.get("command"))) {
                    currentPermissions.get("forbidden").exec(this, map);
                    continue;
                }
                currentPermissions.get(map.get("command")).exec(this, map);
            }
        } catch (MyException e) {
            Console.err("Client " + socket.getInetAddress().getHostAddress()
                    + " disconnected // Reason: " + e.getMessage());
        } catch (Exception ignore) {
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
