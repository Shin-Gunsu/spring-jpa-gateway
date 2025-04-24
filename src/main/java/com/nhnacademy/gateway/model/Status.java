package com.nhnacademy.gateway.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    REGISTERED, DORMANT, WITHDRAWN;

    @JsonCreator
    public static Status fromString(String str){
        for (Status status : Status.values()) {
            if (status.name().equalsIgnoreCase(str)) {
                return status;
            }
        }
        return REGISTERED;
    }


    public String toString(){
        return this.name().toLowerCase();
    }
}
