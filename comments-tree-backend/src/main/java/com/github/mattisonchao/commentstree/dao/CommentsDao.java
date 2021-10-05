package com.github.mattisonchao.commentstree.dao;

import com.github.mattisonchao.commentstree.model.Comments;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Comments Dao */
public interface CommentsDao extends JpaRepository<Comments, Long> {

  List<Comments> findCommentsByRootCommentsIdIsNull();

  List<Comments> findCommentsByRootCommentsIdIn(List<Long> rootCommentsIds);
}
