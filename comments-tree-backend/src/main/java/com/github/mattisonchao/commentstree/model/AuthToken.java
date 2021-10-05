package com.github.mattisonchao.commentstree.model;

import com.github.mattisonchao.commentstree.env.LoginType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.UsernamePasswordToken;

/** 自定义 Token， 支持多种登陆模式 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthToken extends UsernamePasswordToken {
  private LoginType loginType;
  private String email;
}
