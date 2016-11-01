package ru.falseteam.schedule.server.console.commands;

import ru.falseteam.schedule.server.Console;
import ru.falseteam.schedule.server.console.CommandAbstract;
import ru.falseteam.schedule.server.socket.Connection;
import ru.falseteam.schedule.server.socket.Worker;

import java.net.Socket;
import java.util.LinkedList;

public class Connections extends CommandAbstract {
    public Connections() {
        super("connections");
    }

    @Override
    public void exec(String params) {
        StringBuilder sb = new StringBuilder();

        synchronized (Worker.getClients()) {
            final LinkedList<Connection> clients = Worker.getClients();
            sb.append("Current connection: ").append(clients.size()).append('\n');
            sb.append(Console.DEFAULT_MARGIN).append("Connections from all time: ")
                    .append(Worker.getConnectionsFromAllTime()).append('\n');
        }

        Console.print(sb.toString());
    }
}
