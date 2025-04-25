package com.nhnacademy.gateway.model.project.service;

import com.nhnacademy.gateway.model.exception.ProjectNotFoundException;
import com.nhnacademy.gateway.model.project.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectService {

    private final RestTemplate restTemplate;

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectResponse createProject(ProjectCreateCommand command) {
        ResponseEntity<Project> response = restTemplate.postForEntity(
                "http://localhost:8080/api/projects/",
                command,
                Project.class
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 생성 실패: " + statusCode);
        }
        Project project = response.getBody();
        if(project == null) {
            throw  new RuntimeException("");
        }
        return new ProjectResponse(project);
    }

    public ProjectResponse getProject(String id) {
        ResponseEntity<Project> response = restTemplate.getForEntity(
                "http://localhost:8080/api/projects/{id}",
                Project.class,
                id
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new ProjectNotFoundException(id);
        }
        Project project = response.getBody();
        if(project == null) {
            throw new ProjectNotFoundException(id);
        }
        return new ProjectResponse(project);
    }

    public void updateProjectStatus(Long id, ProjectStatusUpdateCommand command) {
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("statusCode", command.getStatus());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(statusUpdate);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8080/api/projects/{id}",
                HttpMethod.PATCH,
                requestEntity,
                Void.class,
                id
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 상태 수정 실패: " + statusCode);
        }
    }

    public void deleteProject(long id) {
        try {
            restTemplate.delete("http://localhost:8080/api/projects/{id}", id);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProjectNotFoundException(Long.toString(id));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete project: " + id, e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("서버 연결 오류: " + e.getMessage(), e);
        }
    }
}
