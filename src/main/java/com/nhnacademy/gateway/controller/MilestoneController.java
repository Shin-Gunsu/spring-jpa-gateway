package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.milestone.domain.MilestoneCreateCommand;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneDetailResponse;
import com.nhnacademy.gateway.model.milestone.domain.MilestoneListResponse;
import com.nhnacademy.gateway.model.milestone.service.MilestoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;

    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @GetMapping
    public ModelAndView findAllMilestone(@PathVariable("projectId") Long projectId) {
        ModelAndView mav = new ModelAndView("milestones-list");
        List<MilestoneListResponse> milestones = milestoneService.findAllMilestones(projectId);
        mav.addObject("milestones", milestones);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/{milestoneId}/create")
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

    @GetMapping("/{milestoneId")
    public ModelAndView getMilestone(@PathVariable("projectId") Long projectId, @PathVariable("milestoneId") Long milestoneId) {
        ModelAndView mav = new ModelAndView("milestone-view");
        MilestoneDetailResponse milestone = milestoneService.findMilestoneById(projectId,milestoneId);
        mav.addObject("milestone", milestone);
        mav.addObject("projectId", projectId);
        return mav;
    }


}
