package ru.practicum.stats_server.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @NotNull
    private long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
