package com.github.mattisonchao.commentstree.configuration.shiro;

import com.github.mattisonchao.commentstree.manager.AuthManager;
import com.github.mattisonchao.commentstree.manager.imp.UserContext;
import com.github.mattisonchao.commentstree.model.UserInfo;
import com.github.mattisonchao.commentstree.model.UserLoginInfo;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

/** 拦截器,用于将用户注入平台 ThreadLocal */
@Configuration
public class UserLoginInterceptor implements HandlerInterceptor {
  private final AuthManager authManager;

  public UserLoginInterceptor(AuthManager authManager) {
    this.authManager = authManager;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    Object principal = SecurityUtils.getSubject().getPrincipal();
    if (principal != null
        && (SecurityUtils.getSubject().isAuthenticated()
            || SecurityUtils.getSubject().isRemembered())) {
      UserLoginInfo userLoginInfo = authManager.findUserLoginInfoByPrincipal((String) principal);
      if (userLoginInfo != null) {
        Long userId = userLoginInfo.getUserId();
        Optional<UserInfo> userInfo = authManager.findUserInfoById(userId);
        UserContext.setCurrentUser(userInfo.orElse(null));
      }
    }
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    UserContext.clear();
  }
}
