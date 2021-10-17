package com.github.lette1394.storage.usecase

import com.github.lette1394.storage.domain.BinaryPublisher
import com.github.lette1394.storage.infra.BrokenPublisher
import reactor.netty.ByteBufFlux

import java.nio.file.Paths

import static com.github.lette1394.core.domain.FluentCompletionStage.await

abstract class UploadingTest extends LeakAwareTest {
  def 'non-null'() {
    def stream = ByteBufFlux
      .fromPath(Paths.get(getClass().getResource("/image/1.png").toURI()))
      .as(BinaryPublisher::adapt)
    def object = await(subject().upload(anySpace(), stream))

    expect:
      object != null
  }

  def 'broken contents'() {
    given:
      def stream = new BrokenPublisher(byteBufAllocator())
    when:
      await(subject().upload(anySpace(), stream))
    then:
      thrown(CannotUploadException)
  }

  abstract Uploading subject()

  abstract String anySpace()
}
