package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.milestone.domain.MilestoneCreateCommand;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneDetailResponse;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneListResponse;
import com.nhnacademy.gateway.model.milestone.service.MilestoneService;
import com.nhnacademy.gateway.model.task.domain.TaskListResponse;
import com.nhnacademy.gateway.model.task.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final TaskService taskService;

    public MilestoneController(MilestoneService milestoneService, TaskService taskService) {
        this.milestoneService = milestoneService;
        this.taskService = taskService;
    }

    @GetMapping
    public ModelAndView findAllMilestone(@PathVariable("projectId") Long projectId) {
        ModelAndView mav = new ModelAndView("milestones-list");
        List<MilestoneListResponse> milestones = milestoneService.findAllMilestones(projectId);
        mav.addObject("milestones", milestones);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createMilestoneForm(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId) {
        ModelAndView mav = new ModelAndView("milestone-create");
        mav.addObject("projectId", projectId);
        mav.addObject("milestoneId", milestoneId);
        return mav;
    }

    @PostMapping
    public ModelAndView createMilestone(@PathVariable("projectId") Long projectId, @ModelAttribute MilestoneCreateCommand command) {
        ModelAndView mav = new ModelAndView("milestone-view");
        MilestoneDetailResponse milestone = milestoneService.addMilestoneToProject(projectId,command);
        mav.addObject("milestone", milestone);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/{milestoneId}")
    public ModelAndView getMilestone(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId) {
        ModelAndView mav = new ModelAndView("milestone-view");
        MilestoneDetailResponse milestone = milestoneService.findMilestoneById(projectId,milestoneId);
        mav.addObject("milestone", milestone);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @DeleteMapping("/{milestoneId}")
    public String deleteMilestone(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId) {
        milestoneService.deleteMilestone(projectId,milestoneId);
        return "redirect:/projects/" + projectId + "/milestones";
    }

    @GetMapping("/{milestoneId}/tasks")
    public ModelAndView findAllMilestoneTasks(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestonedId){
        List<TaskListResponse> tasks = taskService.findAllByMilestone(projectId,milestonedId);
        ModelAndView mav = new ModelAndView("milestone-tasks-list");
        mav.addObject("tasks", tasks);
        mav.addObject("projectId", projectId);
        mav.addObject("milestoneId", milestonedId);
        return mav;
    }

    @PostMapping("/{milestoneId}/tasks")
    public String addTaskToMilestone(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId, @RequestParam("taskId") Long taskId) {
        milestoneService.addTaskToMilestone(projectId,milestoneId,taskId);
        return "redirect:/projects/" + projectId + "/milestones/" + milestoneId + "/tasks";
    }

    @DeleteMapping("/{milestoneId}/tasks/{taskId}")
    public String deleteTaskToMilestone(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId, @PathVariable("taskId") Long taskId) {
        milestoneService.deleteTaskToMilestone(projectId,milestoneId,taskId);
        return "redirect:/projects/" + projectId + "/milestones/" + milestoneId + "/tasks";
    }
}
