package com.github.mattisonchao.commentstree.exception;

import com.github.mattisonchao.commentstree.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BaseException.class)
  ResponseEntity<?> handleServiceException(BaseException ex) {
    Response responseResult =
        Response.builder()
            .code(ex.getErrorCode())
            .statusCode(ex.getStatusCode())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(
            ex.getStatusCode() != 0 ? ex.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR.value())
        .contentType(MediaType.APPLICATION_JSON)
        .body(responseResult);
  }
}
