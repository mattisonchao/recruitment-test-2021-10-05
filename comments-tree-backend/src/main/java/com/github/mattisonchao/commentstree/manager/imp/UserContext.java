package com.github.mattisonchao.commentstree.manager.imp;

import com.github.mattisonchao.commentstree.model.UserInfo;

/** 存储用户信息 */
public class UserContext {
  private static final ThreadLocal<UserInfo> currentUser = new ThreadLocal<>();

  public static UserInfo getCurrentUser() {
    return currentUser.get();
  }

  public static void setCurrentUser(UserInfo user) {
    currentUser.set(user);
  }

  public static void clear() {
    currentUser.remove();
  }
}
