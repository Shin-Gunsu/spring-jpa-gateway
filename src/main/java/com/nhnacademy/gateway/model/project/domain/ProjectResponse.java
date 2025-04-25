package com.nhnacademy.gateway.model.project.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProjectResponse {
    private final long id;
    private final String name;
    @JsonSerialize(using = ToStringSerializer.class)
    private final ProjectStatus statusCode;
    private final Map<String, String> links;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.statusCode = project.getStatusCode();

        this.links = new HashMap<>();
        this.links.put("self", "/api/projects/" + id);
        this.links.put("tasks", "/api/projects/" + id + "/tasks");
        this.links.put("members", "/api/projects/" + id + "/members");
        this.links.put("tags", "/api/projects/" + id + "/tags");
    }
}