package com.example.RoomioStayzy.apis;

import com.example.RoomioStayzy.DTO.CommentDTO;
import com.example.RoomioStayzy.entities.Comment;
import com.example.RoomioStayzy.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Create a new comment
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDTO comment) throws Exception {
        Comment savedComment = commentService.saveComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    // Get all comments
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Get a single comment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a comment by ID
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) throws Exception {
        Optional<Comment> existingComment = commentService.getCommentById(id);
        if (existingComment.isPresent()) {
            updatedComment.setId(id);
            Comment savedComment = commentService.updateComment(updatedComment);
            return new ResponseEntity<>(savedComment, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/housing/{housingId}")
    public ResponseEntity<List<Comment>> getCommentsByHousing(@PathVariable Long housingId) {
        List<Comment> comments = commentService.getCommentsByHousingId(housingId);
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build(); // No comments found for this housing
        }
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
