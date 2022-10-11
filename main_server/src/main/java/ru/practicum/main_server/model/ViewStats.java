package ru.practicum.main_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {

    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    private int hits;
}
