package dev.jramde.saas.service.impl;

import dev.jramde.saas.auth.repository.JrUserRepository;
import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrRegisterTenantRequest;
import dev.jramde.saas.dto.response.JrTenantResponse;
import dev.jramde.saas.entity.JrTenant;
import dev.jramde.saas.entity.JrUser;
import dev.jramde.saas.entity.enums.ERole;
import dev.jramde.saas.entity.enums.ETenantStatus;
import dev.jramde.saas.exception.JrAlreadyExistException;
import dev.jramde.saas.exception.JrInvalidRequestException;
import dev.jramde.saas.mapper.JrTenantMapper;
import dev.jramde.saas.repository.JrTenantRepository;
import dev.jramde.saas.service.IProvisioningService;
import dev.jramde.saas.service.ITenantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JrTenantServiceImpl implements ITenantService {
    public static final String TENANT_NOT_FOUND = "Tenant not found.";
    private final JrTenantRepository tenantRepository;
    private final JrUserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Declare the bean
    private final JrTenantMapper mapper;
    private final IProvisioningService provisioningService;


    /**
     * Register a new tenant by given the organization information.
     *
     * @param request : The organization information.
     */
    @Override
    @Transactional
    public void registerTenant(JrRegisterTenantRequest request) {
        if (tenantRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new JrAlreadyExistException("This code is already used.");
        }

        if (tenantRepository.existsByCompanyEmail(request.getCompanyEmail())) {
            throw new JrAlreadyExistException("This email is already used.");
        }

        final var tenant = mapper.maps(request);
        tenant.setAdminPassword(passwordEncoder.encode(request.getAdminPassword()));
        tenant.setStatus(ETenantStatus.PENDING);
        tenantRepository.save(tenant);
    }

    /**
     * Super admin approves a tenant registration.
     * Ici, pas besoin de transaction car on JDBC va executer les instructions SQL et créer sa propre transaction.
     *
     * @param tenantId : The tenant id.
     */
    @Override
    public void approveTenant(String tenantId) {
        final var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException(TENANT_NOT_FOUND));
        tenant.setStatus(ETenantStatus.ACTIVE);
        tenantRepository.save(tenant);

        try {
            // Provide schema for the tenant (JDBC)
            provisioningService.provideTenant(tenant);
            // Create initial admin user and credentials
            createInitialAdminUser(tenant);
        } catch (final Exception e) {
            rollBackTenantStatus(tenant);
            log.error("Failed to create initial admin user for tenant: {}", tenant.getCompanyName(), e);
        }
    }

    /**
     * Super admin activate a tenant.
     *
     * @param tenantId : The tenant id.
     */
    @Override
    @Transactional
    public void activateTenant(String tenantId) {
        final var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException(TENANT_NOT_FOUND));

        if (tenant.getStatus() != ETenantStatus.PENDING) {
            throw new JrInvalidRequestException("Tenant is not in pending state.");
        }

        tenant.setStatus(ETenantStatus.ACTIVE);
        tenantRepository.save(tenant);
    }

    /**
     * Super admin deactivate a tenant.
     *
     * @param tenantId : The tenant id.
     */
    @Override
    @Transactional
    public void deactivateTenant(String tenantId) {
        final var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException(TENANT_NOT_FOUND));

        if (tenant.getStatus() != ETenantStatus.ACTIVE) {
            throw new JrInvalidRequestException("Tenant is not in active state.");
        }

        tenant.setStatus(ETenantStatus.INACTIVE);
        tenantRepository.save(tenant);
    }

    /**
     * Super admin suspend a tenant.
     *
     * @param tenantId : The tenant id.
     */
    @Override
    @Transactional
    public void suspendTenant(String tenantId) {
        final var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new EntityNotFoundException(TENANT_NOT_FOUND));

        if (tenant.getStatus() != ETenantStatus.ACTIVE) {
            throw new JrInvalidRequestException("Tenant is not in active state.");
        }

        tenant.setStatus(ETenantStatus.SUSPENDED);
        tenantRepository.save(tenant);
    }

    /**
     * Super admin find all registered tenants.
     *
     * @param page : pagination
     * @param size : pagination
     * @return : The registered tenants paginated.
     */
    @Override
    @Transactional
    public JrPageResponse<JrTenantResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrTenant> tenants = tenantRepository.findAll(pageRequest);
        final Page<JrTenantResponse> responses = tenants.map(mapper::maps);
        return JrPageResponse.of(responses);
    }

    private void createInitialAdminUser(JrTenant tenant) {
        if (userRepository.existsByUsername(tenant.getAdminUsername())) {
            throw new JrAlreadyExistException("This user already exists.");
        }

        final var adminUser = JrUser.builder()
                .username(tenant.getAdminUsername())
                .email(tenant.getAdminEmail())
                .firstName(tenant.getAdminFirstName())
                .lastName(tenant.getAdminLastName())
                .password(tenant.getAdminPassword())
                .role(ERole.ROLE_COMPANY_ADMIN)
                .tenant(tenant)
                .enabled(true)
                .build();
        userRepository.save(adminUser);
        log.info("|> Initial admin user created for tenant: {}", tenant.getCompanyName());
    }

    private void rollBackTenantStatus(final JrTenant tenant) {
        tenant.setStatus(ETenantStatus.PENDING);
        tenantRepository.save(tenant);
    }
}











