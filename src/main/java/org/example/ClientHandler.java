package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private Server server;

    //У каждого клиента свой сокет и потоки ввода-вывода, сервер один на всех
    public ClientHandler(Socket socket, Server server) {
            this.socket = socket;
            this.server = server;
            try {
                // ЯВНО указываем UTF-8
                this.in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );
                this.out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                        true
                );
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка создания потоков для клиента", e);
            }
        }


    public void sendMessage(String message) {
        try {
            if (!socket.isClosed()) {
                out.println(message);
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    private void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
        server.removeClient(this); // ← уведомляем сервер
        if (name != null) {
            server.broadcast(name + " покинул чат.", this);
        }
    }

    @Override
    public void run() {
        try {
            // Приветствие
            out.println("Привет! Вы подключились к чату! Как Вас зовут?");
            name = in.readLine();
            if (name == null || name.trim().isEmpty()) {
                name = "Аноним";
            }
            out.println("Привет, " + name + "! Добро пожаловать в чат. Введите /exit для выхода.");
            server.broadcast(name + " присоединился к чату.", this);
            //
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/exit")) {
                    break;
                }
                server.broadcast(name + ": " + message, this);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, "Клиент отключился: " + (name != null ? name : "неизвестный"));
        } finally {
            disconnect();
        }
    }

    public static String formatMessage(String name, String text) {
        return name + ": " + text;
    }
}