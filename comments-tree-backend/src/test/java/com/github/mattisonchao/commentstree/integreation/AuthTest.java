package com.github.mattisonchao.commentstree.integreation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mattisonchao.commentstree.CommentsTreeApplication;
import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.model.request.LoginParam;
import com.github.mattisonchao.commentstree.model.request.RegisterParam;
import okhttp3.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = CommentsTreeApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

  @Autowired private Environment environment;
  private final ObjectMapper mapper = new ObjectMapper();

  @Order(1)
  @Test
  public void testEmptyRegister() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    RegisterParam param = RegisterParam.builder().build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(2)
  @Test
  public void testWrongUserName() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    RegisterParam param =
        RegisterParam.builder()
            .username("@@@")
            .password("abcaaA1@")
            .email("mattisonchao@gmail.com")
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(3)
  @Test
  public void testWrongPassword() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    RegisterParam param =
        RegisterParam.builder()
            .username("mattisonchao")
            .password("abcaa")
            .email("mattisonchao@gmail.com")
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(4)
  @Test
  public void testWrongEmail() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    RegisterParam param =
        RegisterParam.builder()
            .username("mattisonchao")
            .password("abc123@Abc")
            .email("mattisonchao12e.com")
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(5)
  @Test
  public void testNotRegister() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    LoginParam param =
        LoginParam.builder()
            .principal("mattisonRegister")
            .credentials("abc123@Abc")
            .loginType(LoginType.USERNAME)
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/login")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(6)
  @Test
  public void testRegisterAndDuplicatedRegister() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    RegisterParam param =
        RegisterParam.builder()
            .username("mattisonDupli")
            .password("abc123@Abc")
            .email("mattisonchaoDupli@gmail.com")
            .build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/register")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
    }
    Call call2 = client.newCall(request);
    try (Response response = call2.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(7)
  @Test
  public void testEmptyLogin() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    LoginParam param = LoginParam.builder().build();
    RequestBody body =
        RequestBody.create(
            mapper.writeValueAsString(param), MediaType.parse("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/login")
            .post(body)
            .build();
    Call call = client.newCall(request);
    try (Response response = call.execute()) {
      int code = response.code();
      Assertions.assertEquals(400, code);
    }
  }

  @Order(8)
  @Test
  public void testLogin() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    // username login
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
            .url("http://localhost:" + port + "/api/v1/auth/login")
            .post(loginRequestBody)
            .build();
    Call loginCall = client.newCall(loginRequest);
    try (Response response = loginCall.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
    }
    // email login
    LoginParam emailParam =
        LoginParam.builder()
            .principal("mattisonchao")
            .credentials("abc123@Abc")
            .loginType(LoginType.USERNAME)
            .rememberMe(true)
            .build();
    RequestBody emailRequestBody =
        RequestBody.create(
            mapper.writeValueAsString(emailParam),
            MediaType.parse("application/json; charset=utf-8"));
    Request emailRequest =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/login")
            .post(emailRequestBody)
            .build();
    Call emailCall = client.newCall(emailRequest);
    try (Response response = emailCall.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
    }
  }

  @Order(9)
  @Test
  public void testRememberMe() throws IOException {
    OkHttpClient client = new OkHttpClient();
    String port = environment.getProperty("local.server.port");
    // username login
    LoginParam loginParam =
        LoginParam.builder()
            .principal("mattisonchao")
            .credentials("abc123@Abc")
            .loginType(LoginType.USERNAME)
            .rememberMe(true)
            .build();
    RequestBody loginRequestBody =
        RequestBody.create(
            mapper.writeValueAsString(loginParam),
            MediaType.parse("application/json; charset=utf-8"));
    Request loginRequest =
        new Request.Builder()
            .url("http://localhost:" + port + "/api/v1/auth/login")
            .post(loginRequestBody)
            .build();
    Call loginCall = client.newCall(loginRequest);
    try (Response response = loginCall.execute()) {
      int code = response.code();
      Assertions.assertEquals(200, code);
      Assertions.assertTrue(
          Objects.requireNonNull(response.header("Set-Cookie")).contains("rememberMe"));
    }
  }
}
