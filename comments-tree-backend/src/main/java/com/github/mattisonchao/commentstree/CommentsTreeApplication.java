package com.github.mattisonchao.commentstree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Application */
@SpringBootApplication
@EnableJpaAuditing
public class CommentsTreeApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommentsTreeApplication.class, args);
  }
}
