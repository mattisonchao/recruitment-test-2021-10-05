package com.github.mattisonchao.commentstree.manager;

import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.model.UserInfo;
import com.github.mattisonchao.commentstree.model.UserLoginInfo;
import com.github.mattisonchao.commentstree.model.request.LoginParam;
import com.github.mattisonchao.commentstree.model.request.RegisterParam;
import java.util.Optional;

/** Auth Manager */
public interface AuthManager {
  UserLoginInfo findUserLoginInfoByPrincipal(String username, LoginType username1);

  UserLoginInfo findUserLoginInfoByPrincipal(String principal);

  UserInfo register(RegisterParam registerParam);

  UserInfo login(LoginParam loginParam);

  Optional<UserInfo> findUserInfoById(Long userId);
}
