package com.github.mattisonchao.commentstree.configuration.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattisonchao.commentstree.env.ErrorCode;
import com.github.mattisonchao.commentstree.model.response.Response;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springframework.http.HttpStatus;

/**
 * 自己实现 FormAuthenticationFilter , 使其在未认证时返回 401。
 *
 * @author mattison
 */
@Slf4j
public class CustomFormAuthenticationFilter extends UserFilter {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    if (isLoginRequest(request, response)) {
      // allow them to see the login page.
      return true;
    } else {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      Response responseResult =
          Response.builder()
              .message("未进行身份验证，请登陆！")
              .statusCode(HttpStatus.UNAUTHORIZED.value())
              .code(ErrorCode.NO_AUTHORIZED)
              .build();
      PrintWriter writer = response.getWriter();
      writer.write(objectMapper.writeValueAsString(responseResult));
      return false;
    }
  }
}
