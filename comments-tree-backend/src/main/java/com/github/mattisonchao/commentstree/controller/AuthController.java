package com.github.mattisonchao.commentstree.controller;

import com.github.mattisonchao.commentstree.exception.InvalidParameterException;
import com.github.mattisonchao.commentstree.exception.UnAuthorizedException;
import com.github.mattisonchao.commentstree.manager.AuthManager;
import com.github.mattisonchao.commentstree.manager.imp.UserContext;
import com.github.mattisonchao.commentstree.model.UserInfo;
import com.github.mattisonchao.commentstree.model.request.LoginParam;
import com.github.mattisonchao.commentstree.model.request.RegisterParam;
import com.github.mattisonchao.commentstree.utils.AuthChecker;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Auth Controller */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthManager authManager;

  public AuthController(AuthManager authManager) {
    this.authManager = authManager;
  }

  /** 注册用户 */
  @PostMapping("/register")
  public ResponseEntity<UserInfo> register(@RequestBody RegisterParam registerParam) {
    if (!StringUtils.hasText(registerParam.getUsername())) {
      throw new InvalidParameterException(" 用户名不可为空 ");
    }
    if (!StringUtils.hasText(registerParam.getPassword())) {
      throw new InvalidParameterException(" 密码不可为空 ");
    }
    if (!StringUtils.hasText(registerParam.getEmail())) {
      throw new InvalidParameterException(" Email 不可为空");
    }
    Boolean isLegalUserName = AuthChecker.checkUserName(registerParam.getUsername());
    if (!isLegalUserName) {
      throw new InvalidParameterException(" 用户名不合法：只能使用字母和数字，长度在5~20之间，不能与已有用户名重复!");
    }
    Boolean isLegalEmail = AuthChecker.checkEmail(registerParam.getEmail());
    if (!isLegalEmail) {
      throw new InvalidParameterException(" Email 不合法：请输入正确的 Email 格式！");
    }
    Boolean isLegalPassword = AuthChecker.checkPassword(registerParam.getPassword());
    if (!isLegalPassword) {
      throw new InvalidParameterException("密码不合法：长度在8~20之间，至少包含一个大写、一个小写、一个数字、一个特殊符号");
    }
    try {
      UserInfo userInfo = authManager.register(registerParam);
      return ResponseEntity.ok(userInfo);
    } catch (DataIntegrityViolationException exception) {
      throw new InvalidParameterException(" 该用户已存在,请勿重复注册! ");
    }
  }

  /** 登录 */
  @PostMapping("/login")
  public ResponseEntity<UserInfo> login(@RequestBody LoginParam loginParam) {
    if (loginParam.getLoginType() == null) {
      throw new InvalidParameterException(" 错误的登陆类型 ");
    }
    if (!StringUtils.hasText(loginParam.getPrincipal())) {
      throw new InvalidParameterException(" 错误的用户名/邮箱");
    }
    if (!StringUtils.hasText(loginParam.getCredentials())) {
      throw new InvalidParameterException(" 错误的登录密码 ");
    }
    try {
      UserInfo userInfo = authManager.login(loginParam);
      return ResponseEntity.ok(userInfo);
    } catch (IncorrectCredentialsException | UnknownAccountException e) {
      throw new InvalidParameterException(" 密码错误或未找到该用户 ");
    }
  }

  /** 获取登陆状态 */
  @GetMapping("/status")
  public ResponseEntity<UserInfo> loginStatus() {
    UserInfo currentUser = UserContext.getCurrentUser();
    if (currentUser == null) {
      throw new UnAuthorizedException(" 未进行身份验证，请登陆！ ");
    }
    return ResponseEntity.ok(currentUser);
  }
}
