package dev.jramde.saas.service;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrUserRequest;
import dev.jramde.saas.dto.response.JrUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    void create(final JrUserRequest request);

    void update(final String userId, final JrUserRequest request);

    void delete(final String userId);

    void disable(final String userId);

    void enable(final String userId);

    JrUserResponse findById(final String userId);

    JrPageResponse<JrUserResponse> findAll(final int page, final int size);
}
