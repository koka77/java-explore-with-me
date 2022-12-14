package ru.practicum.main_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    @NotNull
    @Size(max = 1000)
    private String annotation;

    @ManyToOne
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    @Size(max = 1000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    private User initiator;

    @ManyToOne()
    private Location location;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "title", nullable = false)
    @Size(max = 512)
    private String title;
}
