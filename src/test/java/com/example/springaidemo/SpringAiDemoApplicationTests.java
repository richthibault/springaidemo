package com.example.springaidemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.ai.azure.openai.api-key=test-key",
    "spring.ai.azure.openai.endpoint=https://test.openai.azure.com/",
    "spring.ai.azure.store.search.api-key=test-search-key",
    "spring.ai.azure.store.search.endpoint=https://test.search.windows.net"
})
class SpringAiDemoApplicationTests {

    @Test
    void contextLoads() {
    }
}
