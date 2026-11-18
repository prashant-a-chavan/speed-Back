package com.project.speedback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    properties = {
      "spring.config.import=",
      "spring.datasource.url=jdbc:h2:mem:speedback;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
      "spring.flyway.enabled=false",
      "security.jwt.secret=test-jwt-secret-key-with-at-least-32-characters"
    })
@ActiveProfiles("test")
class SpeedBackApplicationTests {

  @Test
  void contextLoads() {}
}
