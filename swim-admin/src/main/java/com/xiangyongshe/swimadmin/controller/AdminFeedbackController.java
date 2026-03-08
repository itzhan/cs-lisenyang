package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.dto.FeedbackReplyRequest;
import com.xiangyongshe.swimadmin.entity.Feedback;
import com.xiangyongshe.swimadmin.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/feedbacks")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    public AdminFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<Feedback>> list(@RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        Page<Feedback> result = feedbackService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reply")
    public ApiResponse<Void> reply(@PathVariable Long id, @Valid @RequestBody FeedbackReplyRequest req) {
        Feedback feedback = feedbackService.getById(id);
        if (feedback == null) {
            return ApiResponse.error(404, "反馈不存在");
        }
        feedback.setReplyContent(req.getReplyContent());
        feedback.setReplyTime(LocalDateTime.now());
        feedback.setStatus(1);
        feedbackService.updateById(feedback);
        return ApiResponse.success();
    }
}
