package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.tag.domain.TagCreateCommand;
import com.nhnacademy.gateway.model.tag.domain.TagResponse;
import com.nhnacademy.gateway.model.tag.service.TagService;
import com.nhnacademy.gateway.model.task.domain.TaskListResponse;
import com.nhnacademy.gateway.model.task.service.TaskService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagService tagService;
    private final TaskService taskService;
    private final RedisTemplate redisTemplate;
    private final String SESSIONID = "sessionId";

    public TagController(TagService tagService, TaskService taskService, RedisTemplate redisTemplate) {
        this.tagService = tagService;
        this.taskService = taskService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    public String addTag(@PathVariable("projectId") Long projectId, @ModelAttribute TagCreateCommand command) {

        tagService.createTag(projectId, command);
        return "redirect:/projects/{projectId}/tags";
    }

    @GetMapping
    public ModelAndView getAllTags(@PathVariable("projectId") Long projectId) {
        List<TagResponse> tags = tagService.findAllByProjectId(projectId);

        ModelAndView mav = new ModelAndView("tags-list");
        mav.addObject("tags", tags);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/{tagId}")
    public ModelAndView getTag(@PathVariable("projectId") Long projectId, @PathVariable("tagId") Long tagId) {
        TagResponse tag = tagService.findById(projectId, tagId);
        ModelAndView mav = new ModelAndView("tag-view");
        mav.addObject("tag", tag);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/{tagId}/tasks")
    public ModelAndView getTasksByTag(@PathVariable("projectId") Long projectId, @PathVariable("tagId") Long tagId) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SESSIONID);
        TagResponse tag = tagService.findById(projectId, tagId);
        List<TaskListResponse> tasks = tagService.findTasksByTagId(projectId, tagId);
        ModelAndView mav = new ModelAndView("tag-tasks-list");
        mav.addObject("tag", tag);
        mav.addObject("tasks", tasks);
        mav.addObject("projectId", projectId);
        return mav;
    }


    @PatchMapping("/{tagId}")
    public ModelAndView updateTag(@PathVariable("projectId") Long projectId, @PathVariable("tagId") Long tagId, @ModelAttribute TagCreateCommand command) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SESSIONID);
        tagService.updateTag(projectId, tagId, command);

        return new ModelAndView("redirect:/projects/" + projectId + "/tags/" + tagId);
    }


    @DeleteMapping("/{tagId}")
    public ModelAndView deleteTag(@PathVariable("projectId") Long projectId, @PathVariable("tagId") Long tagId) {
        String loginMemberId = (String) redisTemplate.opsForValue().get(SESSIONID);
        tagService.deleteTag(projectId, tagId);

        return new ModelAndView("redirect:/projects/" + projectId + "/tags");
    }

}