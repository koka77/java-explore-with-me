package ru.practicum.main_server.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotEmpty
    private Long id;
    private String name;
}
