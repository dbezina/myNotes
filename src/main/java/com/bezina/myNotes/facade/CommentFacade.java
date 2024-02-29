package com.bezina.myNotes.facade;

import com.bezina.myNotes.DTO.CommentDTO;
import com.bezina.myNotes.entities.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public final Logger LOG = LoggerFactory.getLogger(CommentFacade.class);
    public CommentDTO commentToCommentDTO(Comment comment){
      //  LOG.info("commentToCommentDTO ");
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setUsername(comment.getUser().getLogin());

        return  commentDTO;
    }
}
