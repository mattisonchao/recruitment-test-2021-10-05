package com.github.mattisonchao.commentstree.model.request;

import com.github.mattisonchao.commentstree.env.LoginType;
import lombok.Builder;
import lombok.Data;

/** 登陆请求参数 */
@Data
@Builder
public class LoginParam {
  private LoginType loginType;
  private String principal;
  private String credentials;
  private Boolean rememberMe;
}
