package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    @NotEmpty
    private Long eventId;
    private String annotation;
    @NotEmpty
    private Long category;
    private String description;
    private String eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
