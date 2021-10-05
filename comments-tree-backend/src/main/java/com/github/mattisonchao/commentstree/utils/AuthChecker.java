package com.github.mattisonchao.commentstree.utils;

import com.sun.istack.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录/注册 参数效验
 *
 * @author mattison
 */
public class AuthChecker {
  private static final Pattern emailPattern =
      Pattern.compile(
          "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
  private static final Pattern userNamePattern = Pattern.compile("^[0-9A-Za-z]{5,20}$");
  private static final Pattern passwordPattern =
      Pattern.compile(
          "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)"
                  + "(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,20}$");

  public static Boolean checkUserName(@NotNull String username) {
    Matcher matcher = userNamePattern.matcher(username);
    return matcher.matches();
  }

  public static Boolean checkPassword(@NotNull String password) {
    Matcher matcher = passwordPattern.matcher(password);
    return matcher.matches();
  }

  public static Boolean checkEmail(@NotNull String email) {
    Matcher matcher = emailPattern.matcher(email);
    return matcher.matches();
  }
}
