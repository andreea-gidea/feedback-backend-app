package com.endava.endavibe.appUser.role;

import com.endava.endavibe.common.util.enums.RolesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService{
    private final RoleRepository roleRepository;
    public RoleEntity getRoleByName(String role) {
        return roleRepository.findByName(role);
    }

    public RoleEntity getRoleByName(RolesEnum role) {
        return this.getRoleByName(role.toString());
    }

    /* Predefined methods */
    public RoleEntity getAdmin() {
        return this.getRoleByName(RolesEnum.ADMIN);
    }

    public RoleEntity getManager() {
        return this.getRoleByName(RolesEnum.MANAGER);
    }

    public RoleEntity getTeamMember() {
        return this.getRoleByName(RolesEnum.TEAM_MEMBER);
    }

    public RoleEntity getSubscriber() {
        return this.getRoleByName(RolesEnum.SUBSCRIBER);
    }
    public RoleEntity getDefault() {
        return this.getRoleByName(RolesEnum.DEFAULT);
    }
}
