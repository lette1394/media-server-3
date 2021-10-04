package com.github.lette1394.storage.usecase

import com.github.lette1394.storage.infra.BrokenIOException
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static com.github.lette1394.core.domain.FluentCompletionStage.await

abstract class UploadingTest extends Specification {
  def 'non-null'() {
    def contents = "hello".getBytes(StandardCharsets.UTF_8)
    def stream = DataBufferUtils.readInputStream(() -> new ByteArrayInputStream(contents), new DefaultDataBufferFactory(), 1024)
    def object = await(subject().upload(anySpace(), stream))

    expect:
      object != null
  }

  def 'broken contents'() {
    given:
      def contents = new InputStream() {
        int index = 0;

        @Override
        int read() throws IOException {
          if (index == 10) {
            throw new BrokenIOException("broken")
          }
          index++
          return 0
        }
      }

    when:
      def stream = DataBufferUtils.readInputStream(() -> contents, new DefaultDataBufferFactory(), 1)
      await(subject().upload(anySpace(), stream))

    then:
      thrown(CannotUploadException)
  }

  abstract Uploading subject()

  abstract String anySpace()
}
