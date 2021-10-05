package com.github.mattisonchao.commentstree.configuration.shiro;

import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.manager.AuthManager;
import com.github.mattisonchao.commentstree.model.AuthToken;
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
public class EmailRealm extends AuthorizingRealm {
  private final AuthManager authManager;

  public EmailRealm(AuthManager authManager, HashedCredentialsMatcher matcher) {
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
    AuthToken authToken = (AuthToken) authenticationToken;
    UserLoginInfo userLoginInfo =
        authManager.findUserLoginInfoByPrincipal(authToken.getEmail(), LoginType.EMAIL);
    return new SimpleAuthenticationInfo(
        userLoginInfo.getPrincipal(),
        userLoginInfo.getCredentials(),
        ByteSource.Util.bytes(userLoginInfo.getSalt()),
        this.getName());
  }
}
