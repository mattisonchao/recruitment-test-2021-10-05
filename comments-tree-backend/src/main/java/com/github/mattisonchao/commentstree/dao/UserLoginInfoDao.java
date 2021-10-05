package com.github.mattisonchao.commentstree.dao;

import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.model.UserLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/** User Login Info 持久层 */
public interface UserLoginInfoDao extends JpaRepository<UserLoginInfo, Long> {
  UserLoginInfo findUserLoginInfoByPrincipalEqualsAndLoginTypeEquals(
      String principal, LoginType loginType);

  UserLoginInfo findUserLoginInfoByPrincipalEquals(String principal);
}
