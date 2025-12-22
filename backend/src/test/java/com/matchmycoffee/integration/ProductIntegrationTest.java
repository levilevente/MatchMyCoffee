package com.matchmycoffee.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductIntegrationTest {
    @Test
    void dummyTest() {
        String testString = "This is a placeholder test";
        assertEquals("This is a placeholder test", testString, "Strings should match");
    }
}
