package ru.practicum.stats_server.model;

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
@Table(name = "hits")
public class HitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 512)
    @Column(name = "app")
    private String app;

    @Size(max = 512)
    @Column(name = "uri")
    private String uri;

    @Size(max = 30)
    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
