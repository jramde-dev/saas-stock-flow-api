package dev.jramde.saas.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class JrRegisterTenantRequest {

    @NotBlank(message = "Company name is required.")
    private String companyName;

    @NotBlank(message = "Company code is required.")
    private String companyCode;

    @NotBlank(message = "Company email is required.")
    private String email;

    @NotBlank(message = "Admin full name is required.")
    private String adminFullName;

    @NotBlank(message = "Admin email is required.")
    private String adminEmail;

    @NotBlank(message = "Admin username is required.")
    private String adminUsername;

    @NotBlank(message = "Admin password is required.")
    private String adminPassword;
}
