package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrUserRequest;
import dev.jramde.saas.dto.response.JrUserResponse;
import dev.jramde.saas.entity.JrUser;
import org.springframework.stereotype.Component;

@Component
public class JrUserMapper {

    public JrUser maps(JrUserRequest request) {
        return JrUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
    }

    public JrUserResponse maps(JrUser entity) {
        return JrUserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .role(entity.getRole())
                .companyName(entity.getTenant().getCompanyName())
                .build();
    }
}
