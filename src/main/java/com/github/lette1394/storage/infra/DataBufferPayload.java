package com.github.lette1394.storage.infra;

import com.github.lette1394.storage.domain.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

@RequiredArgsConstructor
public class DataBufferPayload implements Payload<DataBuffer> {
  private final DataBuffer dataBuffer;

  @Override
  public DataBuffer body() {
    return dataBuffer;
  }

  @Override
  public void retain() {
    DataBufferUtils.retain(dataBuffer);
  }

  @Override
  public void release() {
    assert DataBufferUtils.release(dataBuffer);
  }
}
