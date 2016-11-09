package ru.falseteam.schedule.server.updater;

import ru.falseteam.schedule.server.Console;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.nio.ByteBuffer;

import static ru.falseteam.schedule.server.StaticSettings.getLastClientPath;

class Connection implements Runnable {

    private SSLSocket socket;

    Connection(SSLSocket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    private void disconnect() {
        try {
            Console.print("[updater] Client " + socket.getInetAddress().getHostAddress() + " disconnected");
            socket.close();
        } catch (IOException ignore) {
        }
    }

    @Override
    public void run() {
        class MyException extends Exception {
            private MyException(String message) {
                super(message);
            }
        }
        try {
            OutputStream sout = new BufferedOutputStream(socket.getOutputStream());
            Console.print("[updater] Client " + socket.getInetAddress().getHostAddress() + " connected");
            String path = getLastClientPath();
            File file = new File(path);
            if (!file.exists()) throw new MyException("File not found " + path);
            InputStream fin = new FileInputStream(file);
            long length = file.length();
            byte[] length2bytes = ByteBuffer.allocate(8).putLong(length).array();
            sout.write(length2bytes, 0, length2bytes.length);
            sout.flush();
            byte[] buff = new byte[1024];
            int count;
            while ((count = fin.read(buff)) != -1) {
                sout.write(buff, 0, count);
            }
            sout.flush();
            sout.close();
            fin.close();
        } catch (MyException e) {
            Console.err("[updater] Client " + socket.getInetAddress().getHostAddress()
                    + " disconnected // Reason: " + e.getMessage());
        } catch (Exception ignore) {
        }
        disconnect();
    }
}
