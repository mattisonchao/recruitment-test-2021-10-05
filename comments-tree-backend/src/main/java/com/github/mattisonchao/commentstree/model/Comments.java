package com.github.mattisonchao.commentstree.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 评论表 */
@Entity
@Data
@Table(
    name = "comments",
    indexes = {@Index(name = "user_id_index", columnList = "user_id")})
@EntityListeners(AuditingEntityListener.class)
public class Comments {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "reply_to")
  private Long replyTo;

  @Column(name = "root_comments_id")
  private Long rootCommentsId;

  @Column(name = "content")
  private String content;

  @Column(name = "create_time")
  @CreatedDate
  private Date createTime;

  @Column(name = "update_time")
  @LastModifiedDate
  private Date updateTime;
}
