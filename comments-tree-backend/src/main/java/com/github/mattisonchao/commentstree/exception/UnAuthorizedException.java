package com.github.mattisonchao.commentstree.exception;

import com.github.mattisonchao.commentstree.env.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 未登录异常 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends BaseException {

  public UnAuthorizedException(String message) {
    super(message);
    this.setStatusCode(HttpStatus.UNAUTHORIZED.value());
    this.setErrorCode(ErrorCode.NO_AUTHORIZED);
  }
}
