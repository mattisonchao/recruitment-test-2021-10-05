package com.github.mattisonchao.commentstree.integreation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattisonchao.commentstree.CommentsTreeApplication;
import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.model.Comments;
import com.github.mattisonchao.commentstree.model.request.LoginParam;
import com.github.mattisonchao.commentstree.model.request.PostCommentsParam;
import com.github.mattisonchao.commentstree.model.request.RegisterParam;
import com.github.mattisonchao.commentstree.model.response.CommentsNode;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = CommentsTreeApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentsTest {
  @Autowired private Environment environment;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final OkHttpClient client = new OkHttpClient.Builder().build();

  private String getPort() {
    return environment.getProperty("local.server.port");
  }

  private Headers loginAndGetCookie() throws IOException {
    RegisterParam param =
        RegisterParam.builder()
            .username("mattisonchao")
            .password("abc123@Abc")
            .email("mattisonchao@gmail.com")
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + getPort() + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {}
    LoginParam loginParam =
        LoginParam.builder()
            .principal("mattisonchao")
            .credentials("abc123@Abc")
            .loginType(LoginType.USERNAME)
            .rememberMe(false)
            .build();
    RequestBody loginRequestBody =
        RequestBody.create(
            mapper.writeValueAsString(loginParam),
            MediaType.parse("application/json; charset=utf-8"));
    Request loginRequest =
        new Request.Builder()
            .url("http://localhost:" + getPort() + "/api/v1/auth/login")
            .post(loginRequestBody)
            .build();
    Call loginCall = client.newCall(loginRequest);
    try (Response response = loginCall.execute()) {
      Headers.Builder headerBuilder = new Headers.Builder();
      for (String header : response.headers("Set-Cookie")) {
        headerBuilder.add("Cookie", header);
      }
      return headerBuilder.build();
    }
  }

  @Test
  public void postNewCommentsAndGet() throws IOException {
    Headers headers = loginAndGetCookie();
    PostCommentsParam postCommentsParam = new PostCommentsParam();
    postCommentsParam.setUserId(1L);
    postCommentsParam.setContent("测试留言");
    RequestBody requestBody =
        RequestBody.create(
            mapper.writeValueAsString(postCommentsParam),
            MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + getPort() + "/api/v1/comments")
            .headers(headers)
            .post(requestBody)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
    }
    Request getCommentsRequest =
        new Request.Builder()
            .url("http://localhost:" + getPort() + "/api/v1/comments")
            .get()
            .build();
    Call getCommentsCall = client.newCall(getCommentsRequest);
    try (Response response = getCommentsCall.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
      String comments = Objects.requireNonNull(response.body()).string();

      List<CommentsNode> commentsNode =
          mapper.readValue(comments, new TypeReference<List<CommentsNode>>() {});
      String content = commentsNode.get(0).getComments().getContent();
      Assertions.assertEquals(content, "测试留言");
    }
  }

  @Test
  public void postRecursiveCommentsAndGet() throws IOException {
    Long[] currentCommentsId = new Long[100];
    Headers headers = loginAndGetCookie();
    for (int i = 0; i < 100; i++) {
      PostCommentsParam postCommentsParam = new PostCommentsParam();
      postCommentsParam.setUserId(1L);
      if (i == 0) {
        postCommentsParam.setReplyTo(null);
      } else {
        postCommentsParam.setReplyTo(currentCommentsId[i - 1]);
      }
      postCommentsParam.setContent("测试留言");
      RequestBody requestBody =
          RequestBody.create(
              mapper.writeValueAsString(postCommentsParam),
              MediaType.parse("application/json; charset=utf-8"));
      Request request =
          new Request.Builder()
              .url("http://localhost:" + getPort() + "/api/v1/comments")
              .headers(headers)
              .post(requestBody)
              .build();
      Call call = client.newCall(request);
      try (Response response = call.execute()) {
        int code = response.code();
        Assertions.assertEquals(200, code);
        String commentsJson = Objects.requireNonNull(response.body()).string();
        Comments comments = mapper.readValue(commentsJson, Comments.class);
        currentCommentsId[i] = comments.getId();
      }
    }
    Request getCommentsRequest =
        new Request.Builder()
            .url("http://localhost:" + getPort() + "/api/v1/comments")
            .get()
            .build();
    Call getCommentsCall = client.newCall(getCommentsRequest);
    try (Response response = getCommentsCall.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
      String comments = Objects.requireNonNull(response.body()).string();
      List<CommentsNode> commentsNode =
          mapper.readValue(comments, new TypeReference<List<CommentsNode>>() {});
      AtomicInteger counter = new AtomicInteger(0);
      recursiveCheckComments(commentsNode, counter);
      Assertions.assertEquals(100, counter.get());
    }
  }

  void recursiveCheckComments(List<CommentsNode> commentsNodes, AtomicInteger counter) {
    counter.incrementAndGet();
    for (CommentsNode commentsNode : commentsNodes) {
      String content = commentsNode.getComments().getContent();
      Assertions.assertEquals(content, "测试留言");
      if (CollectionUtils.isNotEmpty(commentsNode.getSubComments())) {
        recursiveCheckComments(commentsNode.getSubComments(), counter);
      }
    }
  }
}
