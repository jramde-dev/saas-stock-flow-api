package dev.jramde.saas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JrCategoryRequest {
    @NotBlank(message = "Category name is required.")
    @Size(min = 3, max = 255, message = "Category name must be between 3 and 255 characters.")
    private String name;

    private String description;
}
