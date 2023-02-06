package com.endava.endavibe.common.util.enums;

public enum RolesEnum {

    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    TEAM_MEMBER("TEAM_MEMBER"),
    SUBSCRIBER("SUBSCRIBER"),
    DEFAULT("DEFAULT");


    private String role;

    RolesEnum(String role) {
        this.role = role;
    }

}
