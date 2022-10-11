package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    @NotNull
    private Long eventId;
    @NotNull
    @Size(max = 1000)
    private String annotation;
    @NotNull
    private Long category;
    @NotEmpty
    @Size(max = 1000)
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    @NotEmpty
    @Size(max = 512)
    private String title;
}
