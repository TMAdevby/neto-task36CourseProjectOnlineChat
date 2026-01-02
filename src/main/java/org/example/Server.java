package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final String CONFIG_FILE = "settings.txt";
    private static final String LOG_FILE = "file.log";
    private static final int DEFAULT_SERVER_PORT = 8089;
    private int serverPort = DEFAULT_SERVER_PORT;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // Список подключённых клиентов
    private final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {

        Server server = new Server();
        server.readServerPortFromFile(CONFIG_FILE);
        server.startServer();
    }

    public void readServerPortFromFile(String fileName) {
        try {
            String content = Files.readString(Paths.get(fileName)).trim();
            setServerPort(Integer.parseInt(content));
            logger.info("Порт успешно прочитан из settings.txt: " + serverPort);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Файл settings.txt не найден. Используется порт по умолчанию: " + DEFAULT_SERVER_PORT, e);
            this.serverPort = DEFAULT_SERVER_PORT;
        }catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Некорректный формат порта в settings.txt. Используется порт по умолчанию: " + DEFAULT_SERVER_PORT, e);
            this.serverPort = DEFAULT_SERVER_PORT;
        }
    }

    public void startServer() {
        logger.info("Сервер запущен на порту " + serverPort);
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Новое подключение!");
                logger.info("IP клиента: " + clientSocket.getInetAddress().getHostAddress());
                logger.info("Порт клиента: " + clientSocket.getPort());

                //При каждом подключении создаем объект клиента, который многопоточен и запускает метод run()
                ClientHandler clientHandler = new ClientHandler(clientSocket, this); // ← передаём this
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка запуска сервера", e);
        }
    }

    // Метод для рассылки сообщений
    public void broadcast(String message, ClientHandler sender) {
        logMessage(message);
        //Рассылка сообщений всем клиентам кроме себя, синхронизация - клиент не будет удален во время перебора клиентов
        // копия чтоб клиент получил сообщение даже если он удалился
        synchronized (clients) {
            for (ClientHandler client : new ArrayList<>(clients)) { // копия для безопасности
                    client.sendMessage(message);
            }
        }
    }

    // Метод для удаления клиента
    public void removeClient(ClientHandler client) {
    // синхронизация - клиент не будет удален во время перебора клиентов
        synchronized (clients) {
            clients.remove(client);
        }
    }

    // Логирование в файл
    private synchronized void logMessage(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true);
             PrintWriter pWriter = new PrintWriter(writer)) {
            //оборачиваем в PrintWriter для удобного вывода, пишем не через logger тк это просто сообщения
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pWriter.println("[" + timestamp + "] " + message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка записи в лог-файл", e);
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }



}
