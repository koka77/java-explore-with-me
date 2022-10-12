package ru.practicum.main_server.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotEmpty
    private Long id;
    @NotNull
    @Size(max = 100)
    private String name;
}
