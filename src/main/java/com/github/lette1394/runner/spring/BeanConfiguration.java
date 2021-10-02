package com.github.lette1394.runner.spring;

import com.github.lette1394.storage.domain.AllObjects;
import com.github.lette1394.storage.domain.AllSpaces;
import com.github.lette1394.storage.domain.Space;
import com.github.lette1394.storage.infra.MemoryObjects;
import com.github.lette1394.storage.infra.MemorySpaces;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
  @Bean
  public AllSpaces allSpaces() {
    final AllObjects memoryObjects = new MemoryObjects(new ConcurrentHashMap<>());
    return new MemorySpaces(Map.of("my-space", new Space("my-space", memoryObjects)));
  }
}
