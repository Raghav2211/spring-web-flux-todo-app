package com.spring.webflux.todo;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@Import({EdgeApplicationTests.TestConfig.class})
@ActiveProfiles("test")
@Testcontainers
class EdgeApplicationTests {
  public static final int REDIS_PORT = 6379;

  @Container
  public static final GenericContainer REDIS =
      new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
          .waitingFor(Wait.forListeningPort())
          .withExposedPorts(REDIS_PORT);

  @Test
  void contextLoads() {}

  @TestConfiguration
  static class TestConfig {

    @Bean
    @Primary
    public RedissonReactiveClient redissonReactiveClient() {
      var config = new Config();
      config
          .useSingleServer()
          .setAddress(
              String.format(
                  "redis://%s:%s",
                  REDIS.getContainerIpAddress(), REDIS.getMappedPort(REDIS_PORT).toString()));
      return Redisson.create(config).reactive();
    }
  }
}
