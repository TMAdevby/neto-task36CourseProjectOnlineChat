package org.example;

import java.util.logging.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {

    private static final String CONFIG_FILE = "settings.txt";
    private static final int DEFAULT_SERVER_PORT = 8089;
    private int serverPort = DEFAULT_SERVER_PORT;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        Server server = new Server();
        server.readServerPortFromFile(CONFIG_FILE);
    }


    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
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
}
