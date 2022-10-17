package ru.practicum.main_server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.model.Comment;
import ru.practicum.main_server.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByEventOrderByCreated(Event event);
}
