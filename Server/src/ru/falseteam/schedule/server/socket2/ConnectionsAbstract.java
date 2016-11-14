package ru.falseteam.schedule.server.socket2;

import javax.net.ssl.SSLSocket;

public abstract class ConnectionsAbstract implements ConnectionInterface {
    protected final SSLSocket socket;

    public ConnectionsAbstract(SSLSocket socket) {
        this.socket = socket;
        new Thread(this, String.format("Connections with %s",
                socket.getInetAddress().getHostAddress())).start();
    }
}
