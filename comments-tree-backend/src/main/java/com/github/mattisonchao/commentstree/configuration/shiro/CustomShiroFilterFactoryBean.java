package com.github.mattisonchao.commentstree.configuration.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

/**
 * 魔改 Shiro，使其可以支持 Http Rest , 自己实现一个 CustomShiroFilterFactoryBean
 *
 * @author mattison
 */
@Slf4j
public class CustomShiroFilterFactoryBean extends ShiroFilterFactoryBean {
  @Override
  protected AbstractShiroFilter createInstance() throws Exception {

    SecurityManager securityManager = getSecurityManager();
    if (securityManager == null) {
      String msg = "SecurityManager property must be set.";
      throw new BeanInitializationException(msg);
    }

    if (!(securityManager instanceof WebSecurityManager)) {
      String msg = "The security manager does not implement the WebSecurityManager interface.";
      throw new BeanInitializationException(msg);
    }
    FilterChainManager manager = createFilterChainManager();
    PathMatchingFilterChainResolver chainResolver = new CustomPathMatchingFilterChainResolver();
    chainResolver.setFilterChainManager(manager);
    return new SpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
  }

  private static final class SpringShiroFilter extends AbstractShiroFilter {
    private SpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
      super();
      if (webSecurityManager == null) {
        throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
      }
      setSecurityManager(webSecurityManager);
      if (resolver != null) {
        setFilterChainResolver(resolver);
      }
    }
  }
}
