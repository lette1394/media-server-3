package com.github.lette1394.storage.infra

import com.github.lette1394.storage.domain.Object
import com.github.lette1394.storage.domain.Space
import com.github.lette1394.storage.usecase.Uploading
import com.github.lette1394.storage.usecase.UploadingTest

import java.util.concurrent.ConcurrentHashMap

class MemoryUploadingTest extends UploadingTest {
  @Override
  String anySpace() {
    return "my-space"
  }

  @Override
  Uploading subject() {
    return new MemoryUploading(new MemorySpaces(["my-space":new Space("my-space", new MemoryObjects(new ConcurrentHashMap<String, Object>()))]))
  }
}
