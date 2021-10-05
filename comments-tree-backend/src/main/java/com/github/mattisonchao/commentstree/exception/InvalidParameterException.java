package com.github.mattisonchao.commentstree.exception;

import com.github.mattisonchao.commentstree.env.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 错误参数异常 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends BaseException {
  public InvalidParameterException(String message) {
    super(message);
    this.setStatusCode(HttpStatus.BAD_REQUEST.value());
    this.setErrorCode(ErrorCode.INVALID_PARAMETER);
  }
}
