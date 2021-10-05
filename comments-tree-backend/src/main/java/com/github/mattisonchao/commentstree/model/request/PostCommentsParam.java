package com.github.mattisonchao.commentstree.model.request;

import lombok.Data;

/** 创建新的评论参数 */
@Data
public class PostCommentsParam {
  private Long userId;
  private Long replyTo;
  private String content;
}
