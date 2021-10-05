package com.github.mattisonchao.commentstree.exception;

import com.github.mattisonchao.commentstree.env.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 未找到该资源异常 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(String message) {
    super(message);
    this.setStatusCode(HttpStatus.NOT_FOUND.value());
    this.setErrorCode(ErrorCode.RESOURCE_NOT_FOUND);
  }
}
