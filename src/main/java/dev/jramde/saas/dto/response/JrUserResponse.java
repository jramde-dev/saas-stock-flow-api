package dev.jramde.saas.dto.response;

import dev.jramde.saas.entity.enums.ERole;
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
public class JrUserResponse {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private ERole role;
    private String companyName;
}
