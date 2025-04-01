package com.example.RoomioStayzy.repositories;


import com.example.RoomioStayzy.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByHousingId(Long housingId);
    List<Comment> findByStudentId(Long studentId);
}