package com.github.lette1394.storage

import com.github.lette1394.Space
import com.github.lette1394.User
import org.gradle.testkit.runner.GradleRunner
import org.springframework.web.reactive.function.client.WebClient
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.OutputFrame
import org.testcontainers.containers.output.ToStringConsumer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.LazyFuture
import spock.lang.Shared
import spock.lang.Specification

import static java.lang.System.currentTimeMillis
import static org.springframework.util.DigestUtils.md5DigestAsHex
import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort

@Testcontainers
abstract class BaseSpringIT extends Specification {
  @Shared
  private Container server = new GenericContainer<>(bootBuildImage())
    .waitingFor(forListeningPort())
    .withExposedPorts(8080)
    .withLogConsumer(console())
    .with {
      start()
      it
    }

  @Shared
  private String endpoint

  void setup() {
    this.endpoint = "http://%s:%d/".formatted(
      server.containerIpAddress,
      server.firstMappedPort)
  }

  protected User user(Space space) {
    return new User(space, webClient())
  }

  private WebClient webClient() {
    return WebClient
      .builder()
      .baseUrl(endpoint)
      .build()
  }

  private static LazyFuture<String> bootBuildImage() {
    return new LazyFuture<String>() {
      @Override
      protected String resolve() {
        var projectRootDir = projectRootDirectory()
        var imageName = imageName(projectRootDir)

        GradleRunner.create()
          .withProjectDir(projectRootDir)
          .withArguments("-q", "bootBuildImage", "-PdockerImageName=" + imageName)
          .forwardOutput()
          .build()

        return imageName
      }
    }
  }

  private static File projectRootDirectory() {
    File cwd = new File(".")
    while (!new File(cwd, "settings.gradle").isFile()) {
      cwd = cwd.getParentFile()
    }
    return cwd
  }

  private static String imageName(File cwd) {
    return "local/app-%s:%s".formatted(
      md5DigestAsHex(cwd.getAbsolutePath().getBytes()),
      currentTimeMillis())
  }

  private static ToStringConsumer console() {
    return new ToStringConsumer() {
      @Override
      void accept(OutputFrame outputFrame) {
        def bytes = outputFrame.getBytes()
        if (bytes == null) {
          return
        }
        System.out.write(bytes)
      }
    }
  }
}
