package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.project.domain.*;
import com.nhnacademy.gateway.model.project.service.ProjectService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

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

    @GetMapping
    public ModelAndView getProjects() {
        ModelAndView mav = new ModelAndView("projects-list");
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        List<ProjectResponse> response = projectService.findAllProject(memberId);
        mav.addObject("projects", response);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getProject(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("project-view");
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        ProjectResponse response = projectService.findProjectById(memberId,id);
        response.setLinks();
        mav.addObject("project", response);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @PostMapping
    public ModelAndView createProject(@ModelAttribute ProjectCreateCommand command) {
        ModelAndView mav = new ModelAndView("project-view");
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        ProjectResponse createdProject = projectService.createProject(memberId,command);
        createdProject.setLinks();
        mav.addObject("project", createdProject);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @PatchMapping("/{id}")
    public ModelAndView updateProject(@ModelAttribute ProjectStatusUpdateCommand command,
                                      @PathVariable Long id) {
        ModelAndView mav = new ModelAndView("project-view");
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);

        ProjectResponse response =  projectService.updateProjectStatus(memberId,id, command);
        response.setLinks();
        mav.addObject("project", response);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        String memberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        projectService.deleteProject(memberId,id);
        return "redirect:/projects";
    }

    @GetMapping("/create")
    public String createProjectForm() {
        return "project-form";
    }
}