package com.github.mattisonchao.commentstree.configuration.shiro;

import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.manager.AuthManager;
import com.github.mattisonchao.commentstree.model.UserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 多功能登录，使用 Email 登录。
 *
 * @author mattison
 */
@Slf4j
public class UserNamePasswordRealm extends AuthorizingRealm {
  private final AuthManager authManager;

  public UserNamePasswordRealm(AuthManager authManager, HashedCredentialsMatcher matcher) {
    super(matcher);
    this.authManager = authManager;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    String username = (String) authenticationToken.getPrincipal();
    UserLoginInfo userLoginInfo =
        authManager.findUserLoginInfoByPrincipal(username, LoginType.USERNAME);
    if (userLoginInfo == null) {
      return null;
    }
    return new SimpleAuthenticationInfo(
        userLoginInfo.getPrincipal(),
        userLoginInfo.getCredentials(),
        ByteSource.Util.bytes(userLoginInfo.getSalt()),
        this.getName());
  }
}
