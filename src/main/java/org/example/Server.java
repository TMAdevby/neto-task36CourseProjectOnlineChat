package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server {

    private static final String CONFIG_FILE = "settings.txt";
    private static final int DEFAULT_SERVER_PORT = 8089;
    private int serverPort = DEFAULT_SERVER_PORT;

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
        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения порта из settings.txt. Используется порт по умолчанию: 8089");
            e.printStackTrace();
            setServerPort(DEFAULT_SERVER_PORT);
        }
        System.out.println("Server port: " + getServerPort());
    }
}
