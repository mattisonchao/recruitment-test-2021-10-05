package com.github.mattisonchao.commentstree;

import com.github.mattisonchao.commentstree.utils.AuthChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthCheckerTest {

  @Test
  public void testUserNameChecker() {
    Assertions.assertFalse(AuthChecker.checkUserName("abcd"));
    Assertions.assertFalse(AuthChecker.checkUserName("abcccccccccccccccccccc"));
    Assertions.assertFalse(AuthChecker.checkUserName("@"));
    Assertions.assertFalse(AuthChecker.checkUserName("你好"));
    Assertions.assertTrue(AuthChecker.checkUserName("mattison"));
    Assertions.assertTrue(AuthChecker.checkUserName("mattison123"));
  }

  @Test
  public void testEmail() {
    Assertions.assertFalse(AuthChecker.checkEmail("mattison"));
    Assertions.assertFalse(AuthChecker.checkEmail("mattison@"));
    Assertions.assertFalse(AuthChecker.checkEmail("@"));
    Assertions.assertFalse(AuthChecker.checkEmail("你好"));
    Assertions.assertFalse(AuthChecker.checkEmail("mattison@gmail"));
    Assertions.assertTrue(AuthChecker.checkEmail("mattison@gmail.com"));
  }

  @Test
  public void testPassword() {
    Assertions.assertFalse(AuthChecker.checkPassword("abcdefg"));
    Assertions.assertFalse(AuthChecker.checkPassword("abcdefghijklmnopqrstx"));
    Assertions.assertFalse(AuthChecker.checkPassword("abcdefghijk"));
    Assertions.assertFalse(AuthChecker.checkPassword("abcdefghijk1"));
    Assertions.assertFalse(AuthChecker.checkPassword("abcdefghijk1I"));
    Assertions.assertTrue(AuthChecker.checkPassword("abcdefghijk1I@"));
  }
}
