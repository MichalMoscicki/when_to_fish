package com.moscicki.user_service.env;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class EnvTests {

    @Autowired
    private Environment environment;

    @Test
    void shouldLoadTestProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        Assertions.assertThat(activeProfiles).contains("test");

        String appName = environment.getProperty("spring.application.name");
        Assertions.assertThat(appName).isEqualTo("user-service-TEST");
    }

}
