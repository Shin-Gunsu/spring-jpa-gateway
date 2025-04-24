package com.nhnacademy.gateway.model;

import lombok.Getter;

import java.util.Locale;

@Getter
public class Requester {
    private String ip;
    private Locale lang;
    public Requester(String ip, Locale lang) {
        this.ip = ip;
        this.lang = lang;
    }

}
