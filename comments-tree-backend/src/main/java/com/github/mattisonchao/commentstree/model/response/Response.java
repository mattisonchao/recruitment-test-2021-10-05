package com.github.mattisonchao.commentstree.model.response;

import com.github.mattisonchao.commentstree.env.ErrorCode;
import lombok.Builder;
import lombok.Data;

/** 返回数据格式 */
@Data
@Builder
public class Response {
  private ErrorCode code;
  private String message;
  private int statusCode;
}
