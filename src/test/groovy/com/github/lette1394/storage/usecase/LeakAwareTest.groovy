package com.github.lette1394.storage.usecase


import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.PooledByteBufAllocator
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.NettyDataBufferFactory
import spock.lang.Specification

abstract class LeakAwareTest extends Specification {
  private LeakAwareByteBufAllocator byteBufAllocator

  protected ByteBufAllocator byteBufAllocator() {
    this.byteBufAllocator
  }

  protected DataBufferFactory dataBufferFactory() {
    new NettyDataBufferFactory(byteBufAllocator())
  }

  void setup() {
    this.byteBufAllocator = new LeakAwareByteBufAllocator()
  }

  void cleanup() {
    this.byteBufAllocator.reportLeak()
  }

  private static class LeakAwareByteBufAllocator extends PooledByteBufAllocator {
    private final List<ByteBuf> allocated = new ArrayList<>()

    LeakAwareByteBufAllocator() {
      super(false)
    }

    @Override
    protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
      def buffer = super.newHeapBuffer(initialCapacity, maxCapacity)
      trace(buffer)
      return buffer
    }

    @Override
    protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
      def buffer = super.newDirectBuffer(initialCapacity, maxCapacity)
      trace(buffer)
      return buffer
    }

    @Override
    boolean isDirectBufferPooled() {
      return false
    }

    void trace(ByteBuf byteBuf) {
      synchronized (allocated) {
        allocated.add(byteBuf)
      }
    }

    void reportLeak() {
      if (allBufferHasBeenReleased()) {
        return
      }
      throw new AssertionError("There is memory leak" as Object)
    }

    boolean allBufferHasBeenReleased() {
      synchronized (allocated) {
        def sum = allocated
          .stream()
          .map(buf -> buf.refCnt())
          .mapToInt(i -> i.intValue())
          .sum()

        sum == 0
      }
    }
  }
}