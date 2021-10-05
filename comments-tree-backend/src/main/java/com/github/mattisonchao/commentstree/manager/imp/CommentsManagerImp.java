package com.github.mattisonchao.commentstree.manager.imp;

import com.github.mattisonchao.commentstree.dao.CommentsDao;
import com.github.mattisonchao.commentstree.dao.UserInfoDao;
import com.github.mattisonchao.commentstree.exception.ResourceNotFoundException;
import com.github.mattisonchao.commentstree.manager.CommentsManager;
import com.github.mattisonchao.commentstree.model.Comments;
import com.github.mattisonchao.commentstree.model.UserInfo;
import com.github.mattisonchao.commentstree.model.request.PostCommentsParam;
import com.github.mattisonchao.commentstree.model.response.CommentsNode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * Comments manager 实现，处理 Comments 相关逻辑
 *
 * @author mattison
 */
@Service
@Slf4j
public class CommentsManagerImp implements CommentsManager {
  private final CommentsDao commentsDao;
  private final UserInfoDao userInfoDao;

  public CommentsManagerImp(CommentsDao commentsDao, UserInfoDao userInfoDao) {
    this.commentsDao = commentsDao;
    this.userInfoDao = userInfoDao;
  }

  /**
   * 获取数据库所有的 评价
   *
   * @return List[CommentsNode] - 所有的组合后的 CommentsNode
   */
  @Override
  public List<CommentsNode> getCommentsTrees() {
    List<Comments> primaryCommentsList = commentsDao.findCommentsByRootCommentsIdIsNull();
    if (CollectionUtils.isEmpty(primaryCommentsList)) {
      return Collections.emptyList();
    }
    List<Long> primaryCommentsIds =
        primaryCommentsList.stream().map(Comments::getId).collect(Collectors.toList());
    List<Comments> subCommentsList = commentsDao.findCommentsByRootCommentsIdIn(primaryCommentsIds);
    Map<Long, List<Comments>> replyToToCollection =
        subCommentsList.stream().collect(Collectors.groupingBy(Comments::getReplyTo));
    List<CommentsNode> replyToNodeList =
        primaryCommentsList.stream()
            .sorted(Comparator.comparing(Comments::getCreateTime).reversed())
            .map(CommentsNode::of)
            .collect(Collectors.toList());
    replyToNodeList.forEach(
        primaryCommentsId -> recursiveFindSubComments(primaryCommentsId, replyToToCollection));
    return replyToNodeList;
  }

  /**
   * 创建一个新的评论
   *
   * @param postCommentsParam - 新评论参数
   * @return Comments - 评论
   */
  @Override
  public Comments postNewComments(PostCommentsParam postCommentsParam) {
    Optional<UserInfo> userInfo = userInfoDao.findById(postCommentsParam.getUserId());
    if (!userInfo.isPresent()) {
      throw new ResourceNotFoundException(" 未找到该用户! ");
    }
    Comments commentsParam = new Comments();
    commentsParam.setUserId(postCommentsParam.getUserId());
    commentsParam.setUserName(userInfo.get().getUsername());
    if (postCommentsParam.getReplyTo() != null) {
      commentsParam.setReplyTo(postCommentsParam.getReplyTo());
      Optional<Comments> replyComments = commentsDao.findById(postCommentsParam.getReplyTo());
      if (!replyComments.isPresent()) {
        throw new ResourceNotFoundException(" 需要回复的评论已被删除！");
      }
      // 如果回复的评论为根节点，则设置根节点为 replyTo 的 rootCommentsId
      if (replyComments.get().getRootCommentsId() == null) {
        commentsParam.setRootCommentsId(replyComments.get().getId());
      } else {
        // 如果回复的评论不为根节点，则设置根节点为 replyTo 的 rootCommentsId
        commentsParam.setRootCommentsId(replyComments.get().getRootCommentsId());
      }
    }
    commentsParam.setContent(postCommentsParam.getContent());
    return commentsDao.saveAndFlush(commentsParam);
  }

  /**
   * 使用递归组合所有的评论
   *
   * @param rootCommentsNode 根节点
   * @param subCommentsList 所有的子节点
   */
  private void recursiveFindSubComments(
      CommentsNode rootCommentsNode, Map<Long, List<Comments>> subCommentsList) {
    // 获取父节点为 rootCommentsNode 的所有子节点
    List<Comments> specificSubCommentsList =
        subCommentsList.get(rootCommentsNode.getComments().getId());
    if (CollectionUtils.isEmpty(specificSubCommentsList)) {
      return;
    }
    // 如果存在，则包装为 CommentsNode 后赋值给 rootCommentsNode
    List<CommentsNode> subCommentsNodes =
        specificSubCommentsList.stream()
            .sorted(Comparator.comparing(Comments::getCreateTime).reversed())
            .map(CommentsNode::of)
            .collect(Collectors.toList());
    rootCommentsNode.setSubComments(subCommentsNodes);
    // 递归组合
    subCommentsNodes.forEach(
        subCommentsNode -> recursiveFindSubComments(subCommentsNode, subCommentsList));
  }
}
