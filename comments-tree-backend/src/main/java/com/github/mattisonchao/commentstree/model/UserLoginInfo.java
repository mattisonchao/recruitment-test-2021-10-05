package com.github.mattisonchao.commentstree.model;

import com.github.mattisonchao.commentstree.env.LoginType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 用户登陆信息表 */
@Entity
@Data
@Table(
    name = "user_login_info",
    indexes = {@Index(name = "user_credentials_index", columnList = "principal", unique = true)})
@EntityListeners(AuditingEntityListener.class)
public class UserLoginInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.ORDINAL)
  private LoginType loginType;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "principal")
  private String principal;

  @Column(name = "credentials")
  private String credentials;

  @Column(name = "salt")
  private String salt;

  @Column(name = "create_time")
  @CreatedDate
  private Date createTime;

  @Column(name = "update_time")
  @LastModifiedDate
  private Date updateTime;
}
