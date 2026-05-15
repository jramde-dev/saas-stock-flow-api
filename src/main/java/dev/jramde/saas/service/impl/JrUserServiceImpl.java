package dev.jramde.saas.service.impl;

import dev.jramde.saas.auth.repository.JrUserRepository;
import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.config.JrTenantContext;
import dev.jramde.saas.dto.request.JrUserRequest;
import dev.jramde.saas.dto.response.JrUserResponse;
import dev.jramde.saas.entity.JrTenant;
import dev.jramde.saas.entity.JrUser;
import dev.jramde.saas.entity.enums.ERole;
import dev.jramde.saas.exception.JrAlreadyExistException;
import dev.jramde.saas.exception.JrInvalidRequestException;
import dev.jramde.saas.mapper.JrUserMapper;
import dev.jramde.saas.repository.JrTenantRepository;
import dev.jramde.saas.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JrUserServiceImpl implements IUserService {
    public static final String USER_NOT_FOUND_MESSAGE = "This user does not exist.";
    private final JrUserRepository userRepository;
    private final JrTenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JrUserMapper mapper;


    @Override
    public void create(JrUserRequest request) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        log.info("|> Creating user for tenant: {}", tenantId);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new JrAlreadyExistException("This username is already used.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new JrAlreadyExistException("This email is already used.");
        }

        if (request.getRole().equals(ERole.ROLE_PLATFORM_ADMIN)) {
            throw new JrInvalidRequestException("User cannot have this role.");
        }

        final JrUser user = mapper.maps(request);
        user.setTenant(JrTenant.builder().id(tenantId).build());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(String userId, JrUserRequest request) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        log.info("|> Updating user for tenant: {}", tenantId);

        final JrUser user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new JrInvalidRequestException("This user does not belong to this tenant.");
        }

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new JrAlreadyExistException("This username is already used.");
        }

        if (request.getRole().equals(ERole.ROLE_PLATFORM_ADMIN)) {
            throw new JrInvalidRequestException("User cannot have this role.");
        }

        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        userRepository.save(user);
    }

    @Override
    public void delete(String userId) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        log.info("|> Deleting user for tenant: {}", tenantId);

        final JrUser user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new JrInvalidRequestException("This user does not belong to this tenant.");
        }

        user.setDeleted(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public void disable(String userId) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        final JrUser user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new JrInvalidRequestException("This user does not belong to this tenant.");
        }

        user.setEnabled(Boolean.FALSE);
        userRepository.save(user);
    }

    @Override
    public void enable(String userId) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        final JrUser user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (!tenantId.equals(user.getTenant().getId())) {
            throw new JrInvalidRequestException("This user does not belong to this tenant.");
        }

        user.setEnabled(Boolean.TRUE);
        userRepository.save(user);
    }

    @Override
    public JrUserResponse findById(String userId) {
        final JrUser user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (!user.getTenant().getId().equals(JrTenantContext.getCurrentTenant())) {
            throw new JrInvalidRequestException("This user does not belong to this tenant.");
        }

        return mapper.maps(user);
    }

    @Override
    public JrPageResponse<JrUserResponse> findAll(int page, int size) {
        final String tenantId = JrTenantContext.getCurrentTenant();
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrUser> users = userRepository.findAllByTenantId(tenantId, pageRequest);
        final Page<JrUserResponse> responses = users.map(mapper::maps);
        return JrPageResponse.of(responses);
    }

    @Override
    public UserDetails loadUserByUsername(final @NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
    }
}
