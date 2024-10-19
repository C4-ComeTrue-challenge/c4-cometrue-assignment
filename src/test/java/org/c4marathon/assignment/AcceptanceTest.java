package org.c4marathon.assignment;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integration.yml")
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public abstract class AcceptanceTest {
}
