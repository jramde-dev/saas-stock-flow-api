package dev.jramde.saas.auth.service;

import dev.jramde.saas.auth.dto.request.JrLoginRequest;
import dev.jramde.saas.auth.dto.response.JrLoginResponse;

public interface IAuthService {
    JrLoginResponse login(JrLoginRequest loginRequest);
}
