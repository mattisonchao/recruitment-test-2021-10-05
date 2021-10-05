package com.github.mattisonchao.commentstree.configuration.shiro;

import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.model.AuthToken;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;

/** 创建多个 Realm 实现多方式登录 */
public class ModularRealmAuthenticator
    extends org.apache.shiro.authc.pam.ModularRealmAuthenticator {
  @Override
  protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
      throws AuthenticationException {
    assertRealmsConfigured();
    Collection<Realm> realms = getRealms();
    Map<String, Realm> realmNameToSelf =
        realms.stream().collect(Collectors.toMap(Realm::getName, (realm) -> realm));
    AuthToken token = (AuthToken) authenticationToken;
    LoginType loginType = token.getLoginType();
    Realm realm = realmNameToSelf.get(loginType.name());
    return realm != null
        ? doSingleRealmAuthentication(realmNameToSelf.get(loginType.name()), token)
        : doMultiRealmAuthentication(realms, token);
  }
}
