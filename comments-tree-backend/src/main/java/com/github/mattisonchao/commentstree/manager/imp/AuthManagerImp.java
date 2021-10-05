package com.github.mattisonchao.commentstree.manager.imp;

import com.github.mattisonchao.commentstree.constant.ShiroConstant;
import com.github.mattisonchao.commentstree.dao.UserInfoDao;
import com.github.mattisonchao.commentstree.dao.UserLoginInfoDao;
import com.github.mattisonchao.commentstree.env.LoginType;
import com.github.mattisonchao.commentstree.exception.InvalidParameterException;
import com.github.mattisonchao.commentstree.exception.ResourceNotFoundException;
import com.github.mattisonchao.commentstree.manager.AuthManager;
import com.github.mattisonchao.commentstree.model.AuthToken;
import com.github.mattisonchao.commentstree.model.UserInfo;
import com.github.mattisonchao.commentstree.model.UserLoginInfo;
import com.github.mattisonchao.commentstree.model.request.LoginParam;
import com.github.mattisonchao.commentstree.model.request.RegisterParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Auth Manager 实现，主要处理验权相关逻辑
 *
 * @author mattison
 */
@Service
public class AuthManagerImp implements AuthManager {
  private final UserInfoDao userInfoDao;
  private final UserLoginInfoDao userLoginInfoDao;

  public AuthManagerImp(UserInfoDao userInfoDao, UserLoginInfoDao userLoginInfoDao) {
    this.userInfoDao = userInfoDao;
    this.userLoginInfoDao = userLoginInfoDao;
  }

  /**
   * 根据 principal 和 login type 获取相关的 login info
   *
   * @param principal - principal
   * @param loginType - loginType
   * @see LoginType
   * @return UserLoginInfo - 用户登录方式
   */
  @Override
  public UserLoginInfo findUserLoginInfoByPrincipal(String principal, LoginType loginType) {
    return userLoginInfoDao.findUserLoginInfoByPrincipalEqualsAndLoginTypeEquals(
        principal, loginType);
  }

  /**
   * 通过唯一的 principal 获取 userLoginInfo
   *
   * @param principal - 登陆唯一凭据
   * @return UserLoginInfo - 登陆方式
   */
  @Override
  public UserLoginInfo findUserLoginInfoByPrincipal(String principal) {
    return userLoginInfoDao.findUserLoginInfoByPrincipalEquals(principal);
  }


  /**
   * 注册用户
   *
   * @param registerParam - 注册参数
   * @return UserInfo - 用户信息
   */
  @Override
  @Transactional
  public UserInfo register(RegisterParam registerParam) {
    String salt = UUID.randomUUID().toString();
    final String encryptedCredentials =
        new Sha256Hash(registerParam.getPassword(), salt, ShiroConstant.HASH_ITERATOR).toBase64();
    // 创建新用户
    UserInfo userInfoParam = new UserInfo();
    userInfoParam.setNickName(salt.substring(0, 8));
    userInfoParam.setEmail(registerParam.getEmail());
    userInfoParam.setUsername(registerParam.getUsername());
    UserInfo userInfo = userInfoDao.saveAndFlush(userInfoParam);
    final List<UserLoginInfo> userLoginInfos = new ArrayList<>();
    // 添加用户名密码登录方式
    UserLoginInfo usernameLoginInfo = new UserLoginInfo();
    usernameLoginInfo.setUserId(userInfo.getId());
    usernameLoginInfo.setLoginType(LoginType.USERNAME);
    usernameLoginInfo.setPrincipal(registerParam.getUsername());
    usernameLoginInfo.setCredentials(encryptedCredentials);
    usernameLoginInfo.setSalt(salt);
    userLoginInfos.add(usernameLoginInfo);
    // 添加邮箱登录方式
    UserLoginInfo emailLoginInfo = new UserLoginInfo();
    emailLoginInfo.setUserId(userInfo.getId());
    emailLoginInfo.setLoginType(LoginType.EMAIL);
    emailLoginInfo.setPrincipal(registerParam.getEmail());
    emailLoginInfo.setCredentials(encryptedCredentials);
    emailLoginInfo.setSalt(salt);
    userLoginInfos.add(emailLoginInfo);
    userLoginInfoDao.saveAllAndFlush(userLoginInfos);
    return userInfo;
  }

  /**
   * 登录
   *
   * @param loginParam - 登录参数
   * @return UserInfo - 用户信息
   */
  @Override
  public UserInfo login(LoginParam loginParam) {
    AuthToken authToken = new AuthToken();
    authToken.setLoginType(loginParam.getLoginType());
    switch (loginParam.getLoginType()) {
      case EMAIL:
        authToken.setEmail(loginParam.getPrincipal());
        break;
      case USERNAME:
        authToken.setUsername(loginParam.getPrincipal());
        break;
      default:
        throw new InvalidParameterException(" 未知登陆类型 ");
    }
    authToken.setPassword(loginParam.getCredentials().toCharArray());
    if (loginParam.getRememberMe() != null) {
      authToken.setRememberMe(loginParam.getRememberMe());
    }
    Subject subject = SecurityUtils.getSubject();
    subject.login(authToken);
    UserLoginInfo userLoginInfoByPrincipal =
        findUserLoginInfoByPrincipal(loginParam.getPrincipal(), loginParam.getLoginType());
    Optional<UserInfo> userInfo = userInfoDao.findById(userLoginInfoByPrincipal.getUserId());
    if (!userInfo.isPresent()) {
      throw new ResourceNotFoundException(" 未找到该用户信息 ");
    }
    return userInfo.get();
  }


  /**
   * 通过用户 ID 获取用户信息
   *
   * @param userId - 用户 ID
   * @return Optional[UserInfo] - 用户信息
   */
  @Override
  public Optional<UserInfo> findUserInfoById(Long userId) {
    return userInfoDao.findById(userId);
  }
}
