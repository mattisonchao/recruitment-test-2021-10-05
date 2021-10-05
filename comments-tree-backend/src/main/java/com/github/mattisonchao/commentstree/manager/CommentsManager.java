package com.github.mattisonchao.commentstree.manager;

import com.github.mattisonchao.commentstree.model.Comments;
import com.github.mattisonchao.commentstree.model.request.PostCommentsParam;
import com.github.mattisonchao.commentstree.model.response.CommentsNode;
import java.util.List;

/** Comments Manager */
public interface CommentsManager {

  List<CommentsNode> getCommentsTrees();

  Comments postNewComments(PostCommentsParam postCommentsParam);
}
