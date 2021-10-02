package com.github.lette1394.storage

import com.github.lette1394.Space
import org.apache.commons.lang3.RandomStringUtils

import java.util.concurrent.CompletionStage

class ObjectIT extends BaseSpringIT {
  def '사용자는 파일을 저장하고 사용할 수 있다'() {
    def user = user(anySpace())
    def object = user.create(file())
    def contents = object.contents()

    expect:
      contents == file()
  }

  static Space anySpace() {
    new Space("my-space")
  }

  static byte[] file() {
    RandomStringUtils.randomAlphanumeric(10, 20).getBytes()
  }

  private static <T> T await(CompletionStage<T> stage) {
    stage.toCompletableFuture().join()
  }
}
