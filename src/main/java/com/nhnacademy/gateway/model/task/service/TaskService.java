package com.nhnacademy.gateway.model.task.service;

import com.nhnacademy.gateway.model.project.domain.ProjectCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectResponse;
import com.nhnacademy.gateway.model.tag.domain.TagIdOnlyResponse;
import com.nhnacademy.gateway.model.task.domain.TaskCreateCommand;
import com.nhnacademy.gateway.model.task.domain.TaskListResponse;
import com.nhnacademy.gateway.model.task.domain.TaskResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TaskService {

    private final RestTemplate restTemplate;
    private final String X_MEMBER_ID = "X-Member-Id";

    public TaskService(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate) {
        this.restTemplate = restTemplate;
    }

    public TaskResponse createTask(Long projectId, TaskCreateCommand taskCreateCommand) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TaskCreateCommand> requestEntity = new HttpEntity<>(taskCreateCommand, headers);

        ResponseEntity<TaskResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks",
                HttpMethod.POST,
                requestEntity,
                TaskResponse.class,
                projectId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 생성 실패: " + statusCode);
        }
        return response.getBody();
    }

    public List<TaskListResponse> findAllByProjectId(Long projectId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TaskListResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TaskListResponse>>() {},
                projectId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task 목록 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public TaskResponse findById(Long projectId, Long taskId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<TaskResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}",
                HttpMethod.GET,
                requestEntity,
                TaskResponse.class,
                projectId,
                taskId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public void deleteTask(Long projectId, Long taskId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                taskId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task 조회 실패: " + statusCode);
        }
    }

    public List<TaskListResponse> findAllByTagId(Long projectId, Long tagId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TaskListResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags/{tagId}/tasks",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TaskListResponse>>() {},
                projectId,
                tagId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task 목록 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public void addTagToTask(Long projectId, Long taskId, Long tagId ) {
        HttpHeaders headers = new HttpHeaders();
        TagIdOnlyResponse tagRequest = new TagIdOnlyResponse(tagId);
        HttpEntity<TagIdOnlyResponse> requestEntity = new HttpEntity<>(tagRequest, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/tags",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                taskId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task tag 추가 실패: " + statusCode);
        }
    }

    public void deleteTagToTask(Long projectId, Long taskId, Long tagId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/tags/{tagId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                taskId,
                tagId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Task tag 추가 실패: " + statusCode);
        }
    }
}
