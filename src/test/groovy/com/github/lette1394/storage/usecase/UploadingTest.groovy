package com.github.lette1394.storage.usecase


import com.github.lette1394.storage.infra.BrokenPublisher
import com.github.lette1394.storage.infra.DataBufferPayload
import org.springframework.core.io.buffer.DataBufferUtils

import java.nio.charset.StandardCharsets

import static com.github.lette1394.core.domain.FluentCompletionStage.await

abstract class UploadingTest extends LeakAwareTest {
  def 'non-null'() {
    def contents = "hello".getBytes(StandardCharsets.UTF_8)
    def stream = DataBufferUtils.readInputStream(() -> new ByteArrayInputStream(contents), dataBufferFactory(), 1024).map(DataBufferPayload::new)
    def object = await(subject().upload(anySpace(), stream))

    expect:
      object != null
  }

  def 'broken contents'() {
    given:
      def stream = new BrokenPublisher(dataBufferFactory())
    when:
      await(subject().upload(anySpace(), stream))
    then:
      thrown(CannotUploadException)
  }

  abstract Uploading subject()

  abstract String anySpace()
}
