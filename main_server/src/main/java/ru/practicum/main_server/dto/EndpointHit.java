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
public class EndpointHit {

    @NotEmpty
    private long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
