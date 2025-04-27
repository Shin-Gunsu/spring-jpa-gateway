package com.nhnacademy.gateway.model.project.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class ProjectResponse {
    private long id;
    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    private ProjectStatus status;
    private Map<String, String> links;

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.status = project.getStatus();
    }
    public void setLinks() {
        this.links = new HashMap<>();
        this.links.put("members", "/projects/" + id + "/members");
        this.links.put("tasks", "/projects/" + id + "/tasks");
        this.links.put("milestones", "/projects/" + id + "/milestones");
    }
}