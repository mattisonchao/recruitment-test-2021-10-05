package com.github.mattisonchao.commentstree.dao;

import com.github.mattisonchao.commentstree.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/** User Info 持久层 */
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {}
