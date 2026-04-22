package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrRegisterTenantRequest;
import dev.jramde.saas.dto.response.JrTenantResponse;
import dev.jramde.saas.entity.JrTenant;
import dev.jramde.saas.entity.enums.ETenantStatus;
import org.springframework.stereotype.Component;

@Component
public class JrTenantMapper {
    public JrTenant maps(final JrRegisterTenantRequest request) {
        return JrTenant.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .email(request.getEmail())
                .adminFullName(request.getAdminFullName())
                .adminEmail(request.getAdminEmail())
                .adminUsername(request.getAdminUsername())
                .status(ETenantStatus.PENDING)
                .build();
    }

    public JrTenantResponse maps(final JrTenant tenant) {
        return JrTenantResponse.builder()
                .tenantId(tenant.getId())
                .companyName(tenant.getCompanyName())
                .companyCode(tenant.getCompanyCode())
                .email(tenant.getEmail())
                .adminFullName(tenant.getAdminFullName())
                .adminEmail(tenant.getAdminEmail())
                .adminUsername(tenant.getAdminUsername())
                .adminPassword(tenant.getAdminPassword())
                .status(tenant.getStatus())
                .createdAt(tenant.getCreatedAt())
                .build();
    }
}
