package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    @NotNull
    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    @NotNull
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private boolean paid;
    @NotNull
    @Size(max = 512)
    private String title;
    private int views;
    private List<CommentDto> comments;
}
