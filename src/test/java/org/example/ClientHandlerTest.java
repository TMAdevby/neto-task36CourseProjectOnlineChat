package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    @Test
    void shouldFormatMessageCorrectly() {
        // given
        String name = "Alice";
        String text = "Привет, чат!";

        // when
        String result = ClientHandler.formatMessage(name, text);

        // then
        assertEquals("Alice: Привет, чат!", result);
    }

    @Test
    void shouldHandleEmptyMessage() {
        // given
        String name = "Bob";
        String text = "";

        // when
        String result = ClientHandler.formatMessage(name, text);

        // then
        assertEquals("Bob: ", result);
    }
}
