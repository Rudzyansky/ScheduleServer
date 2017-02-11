package ru.falseteam.schedule.server.console.commands;

import org.apache.commons.cli.CommandLine;
import ru.falseteam.vframe.console.CommandAbstract;

public class Online extends CommandAbstract {
    @Override
    public void exec(CommandLine commandLine) {
        // TODO: 05.02.17 oops i did it again
//        StringBuilder sb = new StringBuilder();
//
//        synchronized (Worker.getClients()) {
//            final List<Connection> clients = Worker.getClients();
//            sb.append("Online: ").append(clients.size()).append('\n');
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append("Online at all time: ")
//                    .append(Worker.getConnectionsFromAllTime()).append('\n');
//
//            if (clients.size() > 0) {
//                sb1.append("Connections: ").append('\n');
//                final int[] count = {0};
//                StringBuilder sb2 = new StringBuilder();
//                clients.forEach(connection -> {
//                    ++count[0];
//                    sb2.append(count[0]).append(") ").append(connection.getName()).append('\n');
//                    StringBuilder sb3 = new StringBuilder();
//                    sb3.append("Uptime: ").append(StringUtils.getUptime(connection.getUptime())).append('\n');
//                    sb3.append("Group: ").append(connection.getUser().permissions.name()).append('\n');
//                    if (connection.getUser().exists) {
//                        sb3.append("Name: ").append(connection.getUser().name).append('\n');
//                    }
//                    sb2.append(StringUtils.addMargin(sb3.toString(), SHORT_DEFAULT_MARGIN_LENGTH));
//                });
//                sb1.append(StringUtils.addMargin(sb2.toString(), SHORT_DEFAULT_MARGIN_LENGTH));
//            }
//            sb.append(StringUtils.addMargin(sb1.toString(), DEFAULT_MARGIN_LENGTH));
//        }
//
//        Console.print(sb.toString());
    }
}
