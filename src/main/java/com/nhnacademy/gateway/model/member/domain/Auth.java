package com.nhnacademy.gateway.model.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Auth {
    ADMIN(1),
    MEMBER(2);

    private final int code;

    Auth(int code) {
        this.code = code;
    }

    @JsonCreator
    public static Auth fromString(String str){
        for (Auth auth : Auth.values()) {
            if (auth.name().equalsIgnoreCase(str)) {
                return auth;
            }
        }
        return MEMBER;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
