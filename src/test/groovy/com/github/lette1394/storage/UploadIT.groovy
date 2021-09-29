package com.github.lette1394.storage

import com.github.lette1394.Result
import com.github.lette1394.User
import spock.lang.Specification


class UploadIT extends Specification {
  def '사용자는 파일을 업로드 할 수 있다'() {
    User user = new User()
    Result result = user.upload(file())

    expect:
      result.isSuccess()
  }

  byte[] file() {
    return "hello world".getBytes()
  }
}