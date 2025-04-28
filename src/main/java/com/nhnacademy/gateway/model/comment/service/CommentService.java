package com.nhnacademy.gateway.model.comment.service;

import com.nhnacademy.gateway.model.comment.domain.CommentCreateCommand;
import com.nhnacademy.gateway.model.comment.domain.CommentResponse;
import com.nhnacademy.gateway.model.project.domain.ProjectCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CommentService {

    private final RestTemplate restTemplate;
    private final String X_MEMBER_ID = "X-Member-Id";

    public CommentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<CommentResponse> findAllByTaskId(Long projectId, Long taskId, String memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<CommentResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/comments",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<CommentResponse>>() {},
                projectId,
                taskId
        );

        HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("프로젝트 생성 실패: " + statusCode);
        }
        return response.getBody();
    }

    public void createComment(Long projectId, Long taskId, String loginMemberId, CommentCreateCommand command) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, loginMemberId);

        HttpEntity<CommentCreateCommand> requestEntity = new HttpEntity<>(command, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/comments",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                taskId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("코멘트 생성 실패");
        }
    }

    public void deleteComment(Long projectId, Long taskId, Long commentId, String loginMemberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, loginMemberId);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                taskId,
                commentId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("코멘트 삭제 실패");
        }
    }

    public CommentResponse findById(Long projectId, Long taskId, Long commentId, String memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, memberId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<CommentResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}",
                HttpMethod.GET,
                requestEntity,
                CommentResponse.class,
                projectId,
                taskId,
                commentId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("코멘트 조회 실패");
        }
        return response.getBody();
    }

    public CommentResponse updateComment(Long projectId, Long taskId, Long commentId, String loginMemberId, CommentCreateCommand command) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_MEMBER_ID, loginMemberId);
        HttpEntity<CommentCreateCommand> requestEntity = new HttpEntity<>(command, headers);
        ResponseEntity<CommentResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}/update",
                HttpMethod.POST,
                requestEntity,
                CommentResponse.class,
                projectId,
                taskId,
                commentId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("코멘트 수정 실패");
        }
        return response.getBody();
    }
}
