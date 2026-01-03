package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.*;

public class ChatClient {
    private static final Logger chatLogger = Logger.getLogger("ChatClientFileLogger");

    static {
        try {
            // Настраиваем file.log для клиента
            FileHandler fileHandler = new FileHandler("file.log", true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return record.getMessage() + System.lineSeparator();
                }
            });
            chatLogger.addHandler(fileHandler);
            chatLogger.setUseParentHandlers(false); // не писать в консоль
            chatLogger.setLevel(Level.ALL);
        } catch (IOException e) {
            chatLogger.severe("Не удалось создать file.log");
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8089;

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                        true
                );
                BufferedReader console = new BufferedReader(
                        new InputStreamReader(System.in, StandardCharsets.UTF_8)
                )
        ) {
            // Фоновое чтение + логирование
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);          // пользователю
                        chatLogger.info(message);            // в file.log
                    }
                } catch (IOException ignored) {}
            });
            readerThread.setDaemon(true);
            readerThread.start();

            // Отправка + логирование
            String input;
            while ((input = console.readLine()) != null) {
                if ("/exit".equalsIgnoreCase(input.trim())) {
                    break;
                }
                // Логируем отправленное сообщение как "Я: ..."
                // Но сервер присылает полное сообщение, так что можно не дублировать
                out.println(input);
                // Фактически, вы увидите своё сообщение, когда сервер его рассылёт
            }

        } catch (IOException e) {
            String errorMsg = "Ошибка подключения к серверу";
            System.err.println(errorMsg);
            chatLogger.severe(errorMsg + ": " + e.toString());
        }
    }
}