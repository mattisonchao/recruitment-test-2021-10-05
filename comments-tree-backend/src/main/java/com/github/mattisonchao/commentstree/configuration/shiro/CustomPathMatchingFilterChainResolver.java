package com.github.mattisonchao.commentstree.configuration.shiro;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

/**
 * 魔改 Shiro，使其可以支持 Http Rest , 自己实现一个 FilterChain Resolver
 *
 * @author mattison
 */
@Slf4j
public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
  @Override
  public FilterChain getChain(
      ServletRequest request, ServletResponse response, FilterChain originalChain) {
    FilterChainManager filterChainManager = getFilterChainManager();
    if (!filterChainManager.hasChains()) {
      return null;
    }
    String currentPath = getPathWithinApplication(request);
    for (String pathPattern : filterChainManager.getChainNames()) {
      if (isHttpRequestMatched(pathPattern, currentPath, request)) {
        return filterChainManager.proxy(originalChain, pathPattern);
      }
    }
    return null;
  }

  private boolean isHttpRequestMatched(String chain, String currentPath, ServletRequest request) {
    String[] array = chain.split("::");
    String url = array[0];
    boolean isHttpRequestMatched = true;
    if (array.length > 1) {
      String httpMethod = ((HttpServletRequest) request).getMethod();
      String method = array[1];
      isHttpRequestMatched = method.equals(httpMethod);
    }
    return pathMatches(url, currentPath) && isHttpRequestMatched;
  }
}
