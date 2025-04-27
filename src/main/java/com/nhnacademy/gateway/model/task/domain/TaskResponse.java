package com.nhnacademy.gateway.model.task.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String name;
    private String milestone;

    //TODO 태그목록 가져오기
//    private List<String> tags;

    @JsonProperty("created_at")
    private String createdAt;

    private Map<String, String> links;

    public TaskResponse(Long id, String name,  String createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public TaskResponse(Long id, String name, String milestone, String createdAt) {
        this.id = id;
        this.name = name;
        this.milestone = milestone;
        this.createdAt = createdAt;
    }

    public void setLinks(long projectId) {
        this.links = new HashMap<>();
        this.links.put("comments", "/projects/" + projectId + "/tasks/" + id+ "/comments");
        this.links.put("tags", "/projects/" + projectId + "/tasks/" + id+ "/tags");
        this.links.put("members", "/projects/" + projectId + "/tasks/" + id+ "/members");
    }
}