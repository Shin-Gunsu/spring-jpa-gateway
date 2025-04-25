package com.nhnacademy.gateway.controller;

import ch.qos.logback.core.model.Model;
import com.nhnacademy.gateway.model.project.domain.Project;
import com.nhnacademy.gateway.model.project.domain.ProjectCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectResponse;
import com.nhnacademy.gateway.model.project.domain.ProjectStatusUpdateCommand;
import com.nhnacademy.gateway.model.project.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
@RequestMapping("/projects")
public class ProjectContoller {

    private final ProjectService projectService;
    private final RedisTemplate redisTemplate;
    private final String SEESIONID = "sessionId";

    public ProjectContoller(ProjectService projectService, RedisTemplate redisTemplate) {
        this.projectService = projectService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    public ModelAndView createProject(@ModelAttribute ProjectCreateCommand command, HttpServletResponse request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("project");
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        command.setMemberId(memberId);
        ProjectResponse createdProject = projectService.createProject(command);
        mav.addObject("project", createdProject);
        return mav;
    }

    @PatchMapping("/{id}")
    public String updateProject(@ModelAttribute ProjectStatusUpdateCommand command,
                                @PathVariable Long id) {
        projectService.updateProjectStatus(id, command);
        return "redirect:/projects";
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
}