package com.github.mattisonchao.commentstree.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 用户信息表 */
@Entity
@Table(name = "user_info")
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nick_name")
  private String nickName;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "create_time")
  @CreatedDate
  private Date createTime;

  @Column(name = "update_time")
  @LastModifiedDate
  private Date updateTime;
}
