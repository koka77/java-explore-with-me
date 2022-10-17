package ru.practicum.main_server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.dto.UpdateComment;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.service.ParticipationService;

@RestController
@RequestMapping(path = "/users/")
@Slf4j
public class CommentController {
    private final EventService eventService;
    private final ParticipationService participationService;

      CommentController(EventService eventService, ParticipationService participationService) {
        this.eventService = eventService;
        this.participationService = participationService;
    }

    @PostMapping("{userId}/events/{eventId}/comment")
      CommentDto createComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody CommentDto commentDto) {
        log.info("add comment by userId={} and eventId{}", userId, eventId);
        return participationService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("{userId}/comment/{comId}")
      void deleteComment(@PathVariable Long userId,
                              @PathVariable Long comId) {
        log.info("delete comment by userId={} and comId{}", userId, comId);
        participationService.deleteComment(userId, comId);
    }

    @PatchMapping("{userId}/events/{eventId}/comment")
      CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody UpdateComment updateComment) {
        log.info("update comment by userId={} and eventId{}", userId, eventId);
        return participationService.updateComment(userId, eventId, updateComment);
    }
}
