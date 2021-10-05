package com.github.mattisonchao.commentstree.model.request;

import lombok.Builder;
import lombok.Data;

/** 注册请求参数 */
@Data
@Builder
public class RegisterParam {
  private String username;
  private String password;
  private String email;
}
