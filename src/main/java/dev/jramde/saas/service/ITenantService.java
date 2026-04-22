package dev.jramde.saas.service;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrRegisterTenantRequest;
import dev.jramde.saas.dto.response.JrTenantResponse;

public interface ITenantService {
    void registerTenant(final JrRegisterTenantRequest request);

    void approveTenant(final String tenantId);

    void activateTenant(final String tenantId);

    void deactivateTenant(final String tenantId);

    void suspendTenant(final String tenantId);

    JrPageResponse<JrTenantResponse> findAll(final int page, final int size);
}
