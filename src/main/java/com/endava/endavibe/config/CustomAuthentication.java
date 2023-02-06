package com.endava.endavibe.config;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserService;
import com.endava.endavibe.appUser.role.RoleEntity;
import com.endava.endavibe.common.exception.BadRequestException;
import com.endava.endavibe.common.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class CustomAuthentication implements Authentication {
    private final AppUserEntity appUserEntity;
    private final AppUserService appUserService;

    public CustomAuthentication(AppUserEntity appUserEntity, AppUserService appUserService) {
        this.appUserService = appUserService;
        this.appUserEntity = this.findRegisteredAppUser(appUserEntity);
    }

    public AppUserEntity findRegisteredAppUser(AppUserEntity appUserEntity) {
        Optional<AppUserEntity> optAppUser = appUserService.findByUuid(appUserEntity);
        /* Check if it needs updates */
        if (optAppUser.isPresent() && !appUserEntity.equals(optAppUser.get())) {
            AppUserEntity updated = optAppUser.get();
            updated.setEmail(appUserEntity.getEmail());
            updated.setLastName(appUserEntity.getLastName());
            updated.setFirstName(appUserEntity.getFirstName());
            appUserService.save(updated);
            return updated;
        }

        return optAppUser.orElseGet(() -> {
            appUserService.save(appUserEntity);
            return appUserEntity;
        });
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /* TODO Assign a first admin hardcoded.
         *   First logged user or via sql */
        Collection<RoleEntity> roles = appUserEntity.getRoles();
        if (ObjectUtils.isNotEmpty(roles))
            return roles.stream().map(RoleEntity::getName).map(SimpleGrantedAuthority::new).toList();
        /* This is a temporary solution */
        return List.of(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public Object getCredentials() {
        return appUserEntity.getRoles();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return appUserEntity;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
