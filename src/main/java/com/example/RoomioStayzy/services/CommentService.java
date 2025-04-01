package com.example.RoomioStayzy.services;

import com.example.RoomioStayzy.DTO.CommentDTO;
import com.example.RoomioStayzy.entities.Comment;
import com.example.RoomioStayzy.entities.Housing;
import com.example.RoomioStayzy.entities.User;
import com.example.RoomioStayzy.repositories.CommentRepository;
import com.example.RoomioStayzy.repositories.HousingRepository;
import com.example.RoomioStayzy.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EmailService emailService; // Service gửi email
    private  final UserRepository userRepository;
    private  final HousingRepository housingRepository;
    public CommentService(CommentRepository commentRepository, EmailService emailService, UserRepository userRepository, HousingRepository housingRepository) {
        this.commentRepository = commentRepository;
        this.emailService = emailService;
        this.housingRepository = housingRepository;
        this.userRepository = userRepository;
    }
    public Comment saveComment(CommentDTO commentDTO) throws Exception {
        Optional<Housing> housingOptional = housingRepository.findById(commentDTO.getHouse_Id());
        if (housingOptional.isEmpty()) {
            throw new Exception("Housing not found with ID: " + commentDTO.getHouse_Id());
        }
        Housing housing = housingOptional.get();

        Comment comment = new Comment();
        comment.setContent(commentDTO.getComment());
        comment.setHousing(housing);
        comment.setCreatedAt(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = authentication.getName();
            User student = userRepository.findByUsername(username);
            if (student == null) {
                throw new Exception("Authenticated user not found in the database.");
            }
            comment.setStudent(student);
            User housingOwner = housing.getOwner();
            if (housingOwner != null && housingOwner.getEmail() != null) {
                emailService.sendHtmlEmail(
                        housingOwner.getEmail(),
                        "New Comment Notification",
                        housingOwner.getUsername(),
                        username,
                        commentDTO.getComment(),
                        comment.getCreatedAt().toString(),
                        "http://localhost:8081/house/detail/" + housing.getId()
                );
            } else {
                throw new Exception("Housing owner or their email is null.");
            }
        } else {
            throw new Exception("User is not authenticated.");
        }
        return commentRepository.save(comment);
    }

    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    public List<Comment> getCommentsByHousingId(Long housingId) {
        return commentRepository.findByHousingId(housingId);
    }

    // Delete a comment by ID
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
