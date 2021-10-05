package com.github.mattisonchao.commentstree.configuration.shiro;

import com.github.mattisonchao.commentstree.constant.ShiroConstant;
import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.manager.AuthManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.Filter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 常规配置 Shiro
 *
 * @author mattison
 */
@Configuration
public class ShiroConfig implements WebMvcConfigurer {
  private final AuthManager authManager;
  private final UserLoginInterceptor userLoginInterceptor;
  private static final Long MAX_COOKIE_SECOND = TimeUnit.DAYS.toSeconds(30);

  public ShiroConfig(AuthManager authManager,
                     UserLoginInterceptor userLoginInterceptor) {
    this.authManager = authManager;
    this.userLoginInterceptor = userLoginInterceptor;
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userLoginInterceptor);
  }

  /** 配置 security Manager */
  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    CookieRememberMeManager rememberMeManager =
        (CookieRememberMeManager) securityManager.getRememberMeManager();
    Cookie cookie = rememberMeManager.getCookie();
    cookie.setMaxAge(MAX_COOKIE_SECOND.intValue());
    rememberMeManager.setCipherKey(Base64.decode("Y29tbWVudHNUcmVlV29yaw=="));
    securityManager.setAuthenticator(modularRealmAuthenticator());
    List<Realm> realms = new ArrayList<>();
    realms.add(emailRealm());
    realms.add(userNamePasswordRealm());
    securityManager.setRealms(realms);
    SecurityUtils.setSecurityManager(securityManager);
    ThreadContext.bind(securityManager);
    return securityManager;
  }

  /** 配置多 Realm */
  @Bean
  public ModularRealmAuthenticator modularRealmAuthenticator() {
    ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
    modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
    return modularRealmAuthenticator;
  }

  /** email Realm */
  @Bean
  public Realm emailRealm() {
    EmailRealm emailRealm = new EmailRealm(authManager, hashedCredentialsMatcher());
    emailRealm.setName(LoginType.EMAIL.name());
    return emailRealm;
  }

  /** username/password realm */
  @Bean
  Realm userNamePasswordRealm() {
    UserNamePasswordRealm userNamePasswordRealm =
        new UserNamePasswordRealm(authManager, hashedCredentialsMatcher());
    userNamePasswordRealm.setName(LoginType.USERNAME.name());
    return userNamePasswordRealm;
  }

  /** Shiro Filter Factory bean */
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
    filters.put("custom", new CustomHttpFilter());
    filters.put("user", new CustomFormAuthenticationFilter());
    Map<String, String> shiroFilterDefinitionMap = new LinkedHashMap<>();
    shiroFilterDefinitionMap.put("/api/v1/auth/login", "anon");
    shiroFilterDefinitionMap.put("/api/v1/auth/register", "anon");
    shiroFilterDefinitionMap.put("/api/v1/auth/status", "anon");
    shiroFilterDefinitionMap.put("/api/v1/comments::GET", "custom");
    shiroFilterDefinitionMap.put("/**", "user");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterDefinitionMap);
    return shiroFilterFactoryBean;
  }

  /** matcher */
  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
    matcher.setHashAlgorithmName("SHA-256");
    matcher.setHashIterations(ShiroConstant.HASH_ITERATOR);
    matcher.setStoredCredentialsHexEncoded(false);
    return matcher;
  }
}
