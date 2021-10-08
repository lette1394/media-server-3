package com.github.lette1394.storage.domain;

import java.util.concurrent.CompletionStage;

public interface AllSpaces<BUFFER> {
  CompletionStage<Space<BUFFER>> belongingTo(String name);
}
