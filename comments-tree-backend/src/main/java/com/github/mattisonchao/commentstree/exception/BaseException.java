package com.github.mattisonchao.commentstree.exception;

import com.github.mattisonchao.commentstree.env.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 基础异常 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
  private int statusCode;
  private ErrorCode errorCode;

  public BaseException(String message) {
    super(message);
  }
}
