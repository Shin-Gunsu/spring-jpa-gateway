package com.nhnacademy.gateway.model.milestone.service;

import com.nhnacademy.gateway.model.milestone.domain.MilestoneCreateCommand;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneDetailResponse;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneListResponse;
import com.nhnacademy.gateway.model.task.domain.TaskIdOnlyRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MilestoneService {
    private final RestTemplate restTemplate;
    private final String X_MEMBER_ID = "X-Member-Id";

    public MilestoneService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MilestoneListResponse> findAllMilestones(Long projectId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<List<MilestoneListResponse>> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<MilestoneListResponse>>() {},
                projectId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 조회 실패");
        }
        return response.getBody();
    }


    public MilestoneDetailResponse addMilestoneToProject(Long projectId, MilestoneCreateCommand command) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MilestoneCreateCommand> requestEntity = new HttpEntity<>(command, headers);
        ResponseEntity<MilestoneDetailResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones",
                HttpMethod.POST,
                requestEntity,
                MilestoneDetailResponse.class,
                projectId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 추가 실패");
        }
        return response.getBody();
    }

    public MilestoneDetailResponse findMilestoneById(Long projectId, Long milestoneId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<MilestoneDetailResponse> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones/{milestoneId}",
                HttpMethod.GET,
                requestEntity,
                MilestoneDetailResponse.class,
                projectId,
                milestoneId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 조회 실패");
        }
        return response.getBody();
    }

    public void deleteMilestone(Long projectId, Long milestoneId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones/{milestoneId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                milestoneId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 삭제 실패");
        }
    }


    public void addTaskToMilestone(Long projectId, Long milestoneId, Long taskId) {
        HttpHeaders headers = new HttpHeaders();

        TaskIdOnlyRequest taskIdOnlyRequest = new TaskIdOnlyRequest(taskId);
        
        HttpEntity<TaskIdOnlyRequest> requestEntity = new HttpEntity<>(taskIdOnlyRequest, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones/{milestoneId}/tasks",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                milestoneId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 태스크 추가 실패");
        }
    }

    public void deleteTaskToMilestone(Long projectId, Long milestoneId, Long taskId) {
        HttpHeaders headers = new HttpHeaders();


        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8081/api/projects/{projectId}/milestones/{milestoneId}/tasks/{taskId}/delete",
                HttpMethod.POST,
                requestEntity,
                Void.class,
                projectId,
                milestoneId,
                taskId
        );

        HttpStatusCode statusCode =response.getStatusCode();
        if(!statusCode.is2xxSuccessful()){
            throw new RuntimeException("마일스톤 태스크 추가 실패");
        }
    }
}
