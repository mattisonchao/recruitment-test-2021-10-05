package com.github.mattisonchao.commentstree.controller;

import com.github.mattisonchao.commentstree.exception.InvalidParameterException;
import com.github.mattisonchao.commentstree.manager.CommentsManager;
import com.github.mattisonchao.commentstree.model.Comments;
import com.github.mattisonchao.commentstree.model.request.PostCommentsParam;
import com.github.mattisonchao.commentstree.model.response.CommentsNode;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Comments Controller */
@RestController
@RequestMapping("/api/v1/comments")
public class CommentsController {
  private final CommentsManager commentsManager;

  public CommentsController(CommentsManager commentsManager) {
    this.commentsManager = commentsManager;
  }

  @GetMapping
  public ResponseEntity<List<CommentsNode>> getAllCommentsTree() {
    List<CommentsNode> commentsNodeList = commentsManager.getCommentsTrees();
    return ResponseEntity.ok(commentsNodeList);
  }

  /** 新增评论 */
  @PostMapping
  public ResponseEntity<Comments> postNewComments(
      @RequestBody PostCommentsParam postCommentsParam) {
    if (postCommentsParam.getUserId() == null) {
      throw new InvalidParameterException(" 请输入合法的用户 ID! ");
    }
    if (!StringUtils.hasText(postCommentsParam.getContent())) {
      throw new InvalidParameterException(" 内部不得为空!");
    }
    int contentLength = postCommentsParam.getContent().length();
    if (contentLength > 200 || contentLength < 3) {
      throw new InvalidParameterException(" 留言长度在3~200字之间 ");
    }
    Comments newComments = commentsManager.postNewComments(postCommentsParam);
    return ResponseEntity.ok(newComments);
  }
}
