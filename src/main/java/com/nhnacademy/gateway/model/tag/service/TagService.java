package com.nhnacademy.gateway.model.tag.service;

import com.nhnacademy.gateway.model.tag.domain.TagCreateCommand;
import com.nhnacademy.gateway.model.tag.domain.TagResponse;
import com.nhnacademy.gateway.model.task.domain.TaskListResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TagService {
    private final RestTemplate restTemplate;
    private final String X_MEMBER_ID = "X-Member-Id";

    public TagService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public TagResponse createTag(Long projectId, TagCreateCommand tagCreateCommand) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TagCreateCommand> requestEntity = new HttpEntity<>(tagCreateCommand, headers);

        ResponseEntity<TagResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags",
                HttpMethod.POST,
                requestEntity,
                TagResponse.class,
                projectId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 생성 실패: " + statusCode);
        }
        return response.getBody();
    }

    public List<TagResponse> findAllByProjectId(Long projectId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TagResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TagResponse>>() {},
                projectId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 목록 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public TagResponse findById(Long projectId, Long tagId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<TagResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags/{tagId}",
                HttpMethod.GET,
                requestEntity,
                TagResponse.class,
                projectId,
                tagId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public List<TaskListResponse> findTasksByTagId(Long projectId, Long tagId) {
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
            throw new RuntimeException("태그별 Task 목록 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public TagResponse updateTag(Long projectId, Long tagId, TagCreateCommand tagUpdateCommand) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TagCreateCommand> requestEntity = new HttpEntity<>(tagUpdateCommand, headers);

        ResponseEntity<TagResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags/{tagId}/update",
                HttpMethod.POST,
                requestEntity,
                TagResponse.class,
                projectId,
                tagId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 수정 실패: " + statusCode);
        }
        return response.getBody();
    }

    public void deleteTag(Long projectId, Long tagId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tags/{tagId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                tagId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 삭제 실패: " + statusCode);
        }
    }

    public List<TagResponse> findAllByTaskId(Long projectId, Long taskId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TagResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/tags",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TagResponse>>() {},
                projectId,
                taskId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("태그 수정 실패: " + statusCode);
        }
        return response.getBody();
    }
}