package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.member.domain.Auth;
import com.nhnacademy.gateway.model.member.domain.ProjectMemberCreateCommand;
import com.nhnacademy.gateway.model.member.domain.ProjectMemberInfoResponse;
import com.nhnacademy.gateway.model.project.domain.ProjectCreateCommand;
import com.nhnacademy.gateway.model.project.domain.ProjectResponse;
import com.nhnacademy.gateway.model.project.domain.ProjectStatus;
import com.nhnacademy.gateway.model.project.domain.ProjectStatusUpdateCommand;
import com.nhnacademy.gateway.model.project.service.ProjectService;
import com.nhnacademy.gateway.model.user.User;

import com.nhnacademy.gateway.model.user.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectContoller {

    private final UserService userService;
    private final ProjectService projectService;
    private final RedisTemplate redisTemplate;
    private final String SEESIONID = "sessionId";

    public ProjectContoller(UserService userService, ProjectService projectService, RedisTemplate redisTemplate) {
        this.userService = userService;
        this.projectService = projectService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ModelAndView getProjects() {
        ModelAndView mav = new ModelAndView("projects-list");
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        List<ProjectResponse> response = projectService.findAllProject(loginMemberId);
        mav.addObject("projects", response);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView getProject(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("project-view");
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        ProjectResponse response = projectService.findProjectById(loginMemberId,id);
        response.setLinks();
        mav.addObject("project", response);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @PostMapping
    public ModelAndView createProject(@ModelAttribute ProjectCreateCommand command) {
        ModelAndView mav = new ModelAndView("project-view");
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        ProjectResponse createdProject = projectService.createProject(loginMemberId,command);
        createdProject.setLinks();
        mav.addObject("project", createdProject);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @PatchMapping("/{id}")
    public ModelAndView updateProject(@ModelAttribute ProjectStatusUpdateCommand command,
                                      @PathVariable Long id) {
        ModelAndView mav = new ModelAndView("project-view");
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);

        ProjectResponse response =  projectService.updateProjectStatus(loginMemberId,id, command);
        response.setLinks();
        mav.addObject("project", response);
        mav.addObject("projectStatuses", ProjectStatus.values());
        return mav;
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        projectService.deleteProject(loginMemberId,id);
        return "redirect:/projects";
    }

    @GetMapping("/create")
    public String createProjectForm() {
        return "project-create";
    }

    @GetMapping("/{id}/members")
    public ModelAndView getMembers(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("members-list");
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        List<ProjectMemberCreateCommand> response = projectService.findAllProjectMembers(loginMemberId,id);
        mav.addObject("members", response);
        mav.addObject("id", id);
        return mav;
    }

    @PostMapping("/{id}/members")
    public String addMember(@ModelAttribute ProjectMemberCreateCommand newMember, @PathVariable Long id) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
//        projectService.addProjectMember(loginMemberId,newMember,id);
        return "redirect:/projects/{id}/members";
    }

    @GetMapping("/{id}/members/new")
    public ModelAndView addMemberForm(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("project-member-add");
        mav.addObject("id",id);
        mav.addObject("auths", Auth.values());
        return mav;
    }

    @GetMapping("/{id}/members/{memberId}")
    public ModelAndView getMember(@PathVariable Long id, @PathVariable String memberId) {
        ModelAndView mav = new ModelAndView("user-info");
        User user = userService.getUser(memberId);
        ProjectMemberInfoResponse userInfoResponse = new ProjectMemberInfoResponse(user,null);
        mav.addObject("user", userInfoResponse);
        mav.addObject("id", id);
        return mav;
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public String deleteMember(@PathVariable Long id, @PathVariable String memberId) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SEESIONID);
        projectService.deleteMember(loginMemberId,id,memberId);
        return "redirect:/projects/{id}/members";
    }
}