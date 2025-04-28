package com.nhnacademy.gateway.model.project.service;

import com.nhnacademy.gateway.exception.ProjectNotFoundException;
import com.nhnacademy.gateway.model.member.domain.ProjectMemberCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectResponse;
import com.nhnacademy.gateway.model.project.domain.ProjectStatusUpdateCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProjectService {

    private final RestTemplate restTemplate;
    private final String X_MEMBER_ID = "X-Member-Id";

    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectResponse createProject(String memberId, ProjectCreateCommand command) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<ProjectCreateCommand> requestEntity = new HttpEntity<>(command, headers);

        ResponseEntity<ProjectResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects",
                HttpMethod.POST,
                requestEntity,
                ProjectResponse.class
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 생성 실패: " + statusCode);
        }
        return response.getBody();
    }

    public List<ProjectResponse> findAllProject(String memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProjectResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ProjectResponse>>() {}
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new ProjectNotFoundException("Failed to retrieve projects");
        }
        return response.getBody();
    }


    public ProjectResponse findProjectById(String memberId, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<ProjectResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{id}",
                HttpMethod.GET,
                requestEntity,
                ProjectResponse.class,
                id
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new ProjectNotFoundException("프로젝트 조회 실패: " + statusCode);
        }
        return response.getBody();
    }

    public ProjectResponse updateProjectStatus(String memberId, Long id, ProjectStatusUpdateCommand command) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("status", command.getStatus().toString());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(statusUpdate, headers);

        ResponseEntity<ProjectResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{id}/status",
                HttpMethod.POST,
                requestEntity,
                ProjectResponse.class,
                id
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 상태 수정 실패: " + statusCode);
        }
        return response.getBody();
    }

    @Transactional
    public void deleteProject(String memberId, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        try {
            restTemplate.exchange(
                    "http://localhost:8081/api/projects/{id}/delete",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    id
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProjectNotFoundException(Long.toString(id));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Failed to delete project: " + id, e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("서버 연결 오류: " + e.getMessage(), e);
        }
    }

    public List<ProjectMemberCreateCommand> findAllProjectMembers(String memberId, long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<ProjectMemberCreateCommand>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{id}/members",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ProjectMemberCreateCommand>>() {},
                id
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("findAllProjectMembers");
        }
        return response.getBody();
    }

    @Transactional
    public void addProjectMember(String memberId, ProjectMemberCreateCommand userIdDto , long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);
        HttpEntity<ProjectMemberCreateCommand> requestEntity = new HttpEntity<>(userIdDto, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{id}/members",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                id
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("addProjectMember");
        }
    }

    @Transactional
    public void deleteMember(String memberId, long id, String deleteMemberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{id}/members/{deleteMemberId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                id,
                deleteMemberId
        );
        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("deleteMember" + statusCode);
        }
    }
}
