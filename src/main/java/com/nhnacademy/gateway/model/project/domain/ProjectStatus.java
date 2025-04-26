package com.nhnacademy.gateway.model.project.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    ACTIVATED(1),
    DORMANT(2),
    COMPLETED(3);

    private final int code;

    ProjectStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @JsonCreator
    public static ProjectStatus fromString(String str){
        for (ProjectStatus status : ProjectStatus.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        return ACTIVATED;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
