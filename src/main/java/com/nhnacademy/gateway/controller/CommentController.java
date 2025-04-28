package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.comment.domain.CommentCreateCommand;
import com.nhnacademy.gateway.model.comment.domain.CommentResponse;
import com.nhnacademy.gateway.model.comment.service.CommentService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final RedisTemplate redisTemplate;

    public CommentController(CommentService commentService, RedisTemplate redisTemplate) {
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping
    public ModelAndView getComments(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        ModelAndView mav = new ModelAndView("comments-list");
        String loginMemberId = (String) redisTemplate.opsForValue().get("SEESIONID");
        List<CommentResponse> comments = commentService.findAllByTaskId(projectId,taskId,loginMemberId);
        mav.addObject("comments", comments);
        mav.addObject("projectId", projectId);
        mav.addObject("taskId", taskId);
        return mav;
    }

    @PostMapping
    public String addComment(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,@ModelAttribute CommentCreateCommand command) {
        ModelAndView mav = new ModelAndView("comments-list");
        String loginMemberId = (String) redisTemplate.opsForValue().get("SEESIONID");
        commentService.createComment(projectId,taskId,loginMemberId,command);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/comments";
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId,@PathVariable("commentId") Long commentId) {
        String loginMemberId = (String) redisTemplate.opsForValue().get("SEESIONID");
        commentService.deleteComment(projectId,taskId,commentId,loginMemberId);
        return "redirect:/projects/" + projectId + "/tasks/" + taskId + "/comments";
    }

    @PatchMapping("/{commentId}")
    public ModelAndView updateComment(@PathVariable("projectId") Long projectId,
                                      @PathVariable("taskId") Long taskId,
                                      @PathVariable("commentId") Long commentId,
                                      @ModelAttribute CommentCreateCommand command) {
        String loginMemberId = (String) redisTemplate.opsForValue().get("SEESIONID");
        CommentResponse comment = commentService.updateComment(projectId,taskId,commentId,loginMemberId,command);
        ModelAndView mav = new ModelAndView("comment-view");
        mav.addObject("comment", comment);
        mav.addObject("projectId", projectId);
        mav.addObject("taskId", taskId);
        return mav;
    }

    @GetMapping("/{commentId}")
    public ModelAndView getComment(@PathVariable("projectId") Long projectId,
                                          @PathVariable("taskId") Long taskId,
                                          @PathVariable("commentId") Long commentId) {
        String loginMemberId = (String) redisTemplate.opsForValue().get("SEESIONID");
        ModelAndView mav = new ModelAndView("comment-view");
        CommentResponse comment = commentService.findById(projectId,taskId,commentId,loginMemberId);
        mav.addObject("projectId", projectId);
        mav.addObject("taskId", taskId);
        mav.addObject("comment", comment);
        mav.addObject("loginMemberId", loginMemberId);
        return mav;
    }
}
