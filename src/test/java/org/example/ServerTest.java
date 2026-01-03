package org.example;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private Server server;
    private Path tempConfig;

    @BeforeEach
    void setUp() throws IOException {
        server = new Server();
        // Создаём временный файл настроек
        tempConfig = Files.createTempFile("settings", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempConfig);
    }

    @Test
    void shouldReadValidPortFromFile() {
        // given
        writeToFile(tempConfig, "8090");

        // when
        server.readServerPortFromFile(tempConfig.toString());

        // then
        assertEquals(8090, server.getServerPort());
    }

    @Test
    void shouldUseDefaultPortIfFileNotFound() {
        // given
        String nonExistentFile = "nonexistent.txt";

        // when
        server.readServerPortFromFile(nonExistentFile);

        // then
        assertEquals(8089, server.getServerPort()); // DEFAULT_SERVER_PORT
    }

    @Test
    void shouldUseDefaultPortIfInvalidNumber() {
        // given
        writeToFile(tempConfig, "not_a_number");

        // when
        server.readServerPortFromFile(tempConfig.toString());

        // then
        assertEquals(8089, server.getServerPort());
    }

    @Test
    void shouldUseDefaultPortIfEmptyFile() {
        // given
        writeToFile(tempConfig, "");

        // when
        server.readServerPortFromFile(tempConfig.toString());

        // then
        assertEquals(8089, server.getServerPort());
    }

    private void writeToFile(Path path, String content) {
        try {
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}