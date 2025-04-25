package com.nhnacademy.gateway.model.project.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    ACTIVATED,DORMANT,COMPLETED;

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
