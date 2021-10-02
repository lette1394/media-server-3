package com.github.lette1394.storage


import java.util.concurrent.CompletionStage

import static com.github.lette1394.storage.domain.Space.space

class ObjectIT extends BaseSpringIT {
  def '사용자는 파일을 저장하고 사용할 수 있다'() {
    def user = user(space("my-space"))
    def object = user.create(file())
    def contents = object.contents()

    expect:
      contents == file()
  }

  byte[] file() {
    return "hello world".getBytes()
  }

  private static <T> T await(CompletionStage<T> stage) {
    return stage.toCompletableFuture().join()
  }
}
