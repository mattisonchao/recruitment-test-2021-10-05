package com.github.mattisonchao.commentstree.model.response;

import com.github.mattisonchao.commentstree.model.Comments;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 评论树节点 */
@Data
@NoArgsConstructor
public class CommentsNode {
  private Comments comments;
  private List<CommentsNode> subComments;

  /**
   * 工厂模式 - 创建一个树节点
   *
   * @param comments - 评论
   * @return CommentsNode 评论树节点
   */
  public static CommentsNode of(Comments comments) {
    CommentsNode commentsNode = new CommentsNode();
    commentsNode.setComments(comments);
    return commentsNode;
  }
}
