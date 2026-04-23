package dev.jramde.saas.service;

import dev.jramde.saas.entity.JrTenant;

public interface IProvisioningService {
    void provideTenant(final JrTenant tenant);
}
