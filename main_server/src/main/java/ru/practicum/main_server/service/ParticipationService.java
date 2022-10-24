package ru.practicum.main_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CommentDto;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.dto.UpdateComment;
import ru.practicum.main_server.exception.RejectedRequestException;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.WrongRequestException;
import ru.practicum.main_server.mapper.CommentMapper;
import ru.practicum.main_server.mapper.ParticipationMapper;
import ru.practicum.main_server.model.*;
import ru.practicum.main_server.repository.CommentRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.ParticipationRepository;
import ru.practicum.main_server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserService userService;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public ParticipationService(ParticipationRepository participationRepository, UserRepository userRepository, UserService userService, EventService eventService, EventRepository eventRepository, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.participationRepository = participationRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<ParticipationRequestDto> getParticipationRequestsByUser(Long userId) {
        User requester = userService.checkAndGetUser(userId);
        return participationRepository.findAllByRequester(requester)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User requester = userService.checkAndGetUser(userId);
        Event event = eventService.checkAndGetEvent(eventId);
        if (participationRepository.findByEventAndRequester(event, requester) != null) {
            throw new WrongRequestException("Participation request in event id = "
                    + eventId + "by user id = " + userId + " already exist");
        }
        if (event.getInitiator().equals(requester)) {
            throw new WrongRequestException("initiator event can not be requester");
        }
        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new WrongRequestException("you can not request not published event");
        }
        int confirmedRequests = participationRepository.countDistinctByEventAndStatus(event, StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() != null && event.getParticipantLimit() != 0 && event
                .getParticipantLimit() <= confirmedRequests) {
            throw new WrongRequestException("Participant limit already full");
        }
        Participation participation = Participation.builder()
                .event(event)
                .requester(requester)
                .created(LocalDateTime.now())
                .status(StatusRequest.CONFIRMED)
                .build();
        if (event.isRequestModeration()) {
            participation.setStatus(StatusRequest.PENDING);
        }

        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    @Transactional
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("request id = " + requestId + " not found"));
        if (userId.equals(participation.getRequester().getId())) {
            participation.setStatus(StatusRequest.CANCELED);
        } else {
            throw new WrongRequestException("Only owner can cancelled request");
        }
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    public List<ParticipationRequestDto> getEventParticipationByOwner(Long userId, Long eventId) {
        Event event = eventService.checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("Only owner can see requests");
        }
        return participationRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto approvalParticipationEventRequest(Long userId, Long eventId, Long participationId) {
        Event event = eventService.checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("Only owner can see requests");
        }
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ObjectNotFoundException("participation not found"));
        if (!participation.getStatus().equals(StatusRequest.PENDING)) {
            throw new WrongRequestException("Only status pending can be approval");
        }
        int countConfirmedRequests = participationRepository.countByEventIdAndStatus(eventId, StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() >= countConfirmedRequests) {
            participation.setStatus(StatusRequest.REJECTED);
        }
        participation.setStatus(StatusRequest.CONFIRMED);
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    @Transactional
    public ParticipationRequestDto rejectParticipationEventRequest(Long userId, Long eventId, Long participationId) {
        Event event = eventService.checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("Only owner can see requests");
        }
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new ObjectNotFoundException("participation not found"));

        participation.setStatus(StatusRequest.REJECTED);
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {

        if (commentDto.getText().length() == 0 || commentDto.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        if (!eventService.getEventById(eventId).getState().equals(State.PUBLISHED)) {
            throw new RejectedRequestException(String.format("Sorry you no Event no published"));
        }
        User user = userService.findById(userId);
        Event event = eventRepository.findById(eventId).get();
        Comment comment = commentMapper.toComment(commentDto, user, event);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long userId, Long comId) {
        if (!commentRepository.findById(comId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Comment not found id=%s", comId));
        }
        if (!Objects.equals(commentRepository.findById(comId).get().getAuthor().getId(), userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        commentRepository.deleteById(comId);
    }

    public CommentDto updateComment(Long userId, Long eventId, UpdateComment updateComment) {
        if (updateComment.getId() == null) {
            throw new RejectedRequestException("Sorry comment null id");
        }
        if (!commentRepository.findById(updateComment.getId()).isPresent()) {
            throw new ObjectNotFoundException(String.format("Comment not found id=%s", updateComment.getId()));
        }
        if (updateComment.getText().length() == 0 || updateComment.getText() == null) {
            throw new RejectedRequestException("Sorry comment null text");
        }
        if (!eventRepository.findById(eventId).get().getState().equals(State.PUBLISHED)) {
            throw new RejectedRequestException(String.format("Sorry you no Event no published"));
        }
        if (!Objects.equals(commentRepository.findById(updateComment.getId()).get().getAuthor().getId(), userId)) {
            throw new RejectedRequestException("Sorry you no author comment");
        }
        Comment comment = commentRepository.findById(updateComment.getId()).get();
        commentMapper.updateCommentFromUpdateComment(updateComment, comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
