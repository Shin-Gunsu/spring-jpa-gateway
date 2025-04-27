package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.task.domain.TaskCreateCommand;
import com.nhnacademy.gateway.model.task.domain.TaskListResponse;
import com.nhnacademy.gateway.model.task.domain.TaskResponse;
import com.nhnacademy.gateway.model.task.service.TaskService;
import jakarta.websocket.server.PathParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ModelAndView createTask(@PathVariable("projectId") Long projectId, @ModelAttribute TaskCreateCommand task) {
        ModelAndView mav = new ModelAndView("task-view");
        TaskResponse taskResponse = taskService.createTask(projectId, task);
        mav.addObject("projectId", projectId);
        mav.addObject("task", taskResponse);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createTaskForm(@PathVariable("projectId") Long projectId){
        ModelAndView mav = new ModelAndView("task-create");
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping
    public ModelAndView findAllTasks(@PathVariable("projectId") Long projectId){
        ModelAndView mav = new ModelAndView("tasks-list");
        List<TaskListResponse> taskListResponses = taskService.findAllByProjectId(projectId);

        mav.addObject("tasks", taskListResponses);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @GetMapping("/{taskId}")
    public ModelAndView findTasks(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId){
        ModelAndView mav = new ModelAndView("task-view");
        TaskResponse taskResponse = taskService.findById(projectId,taskId);
        taskResponse.setLinks(projectId);
        //TODO 업무 수정 form추가 하거나 view에서 수정할수 있는 기능 추가
//      List<Tag> tags = tagService.findAll(projectId)
//      List<Milestone> milestones = milestoneService.findAll(projectId);
//      mav.addObject("tags",tags);
//      mav.addObject("milestones",milestones);
        mav.addObject("task", taskResponse);
        mav.addObject("projectId", projectId);
        return mav;
    }

    @PatchMapping("/{taskId}")
    public ModelAndView updateTask(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId){
        ModelAndView mav = new ModelAndView("task-view");
        TaskResponse taskResponse = taskService.findById(projectId,taskId);
        return  mav;
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        taskService.deleteTask(projectId,taskId);
        return "redirect:/projects/{projectId}/tasks";
    }

}
