package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_server.model.Location;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotNull
    private String annotation;
    @NotNull
    private long category;
    @Size(max = 1000)
    private String description;
    @NotNull
    private String eventDate;
    private Location location;
    @NotNull
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @Size(max = 20)
    private String state;
    @NotNull
    @Size(max = 512)
    private String title;
}
