package com.example.todo.todoapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoModifyRequestDTO {
    @NotBlank
    private String id;
    
    private boolean done;
}
