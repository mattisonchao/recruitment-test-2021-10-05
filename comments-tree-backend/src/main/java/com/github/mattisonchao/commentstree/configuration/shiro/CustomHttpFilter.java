package com.github.mattisonchao.commentstree.configuration.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

/**
 * 魔改 Shiro，使其可以支持 Http Rest , 自己实现一个 Filter 来判断请求 Method
 *
 * @author mattison
 */
@Slf4j
public class CustomHttpFilter extends PermissionsAuthorizationFilter {
  @Override
  protected boolean pathsMatch(String path, ServletRequest request) {
    String requestUri = getPathWithinApplication(request);
    String[] array = path.split("::");
    boolean isHttpRequestMatched = true;
    if (array.length > 1) {
      String httpMethod = ((HttpServletRequest) request).getMethod();
      String method = array[1];
      isHttpRequestMatched = method.equals(httpMethod);
    }
    return pathsMatch(path, requestUri) && isHttpRequestMatched;
  }
}
