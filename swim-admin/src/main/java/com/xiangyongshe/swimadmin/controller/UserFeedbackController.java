package com.xiangyongshe.swimadmin.controller;

import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.entity.Feedback;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.service.FeedbackService;
import com.xiangyongshe.swimadmin.util.SecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/feedbacks")
public class UserFeedbackController {

    private final FeedbackService feedbackService;
    private final SecurityUtil securityUtil;

    public UserFeedbackController(FeedbackService feedbackService, SecurityUtil securityUtil) {
        this.feedbackService = feedbackService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ApiResponse<Feedback> create(@RequestBody Feedback feedback) {
        SysUser user = securityUtil.currentUser();
        feedback.setUserId(user.getId());
        feedback.setStatus(0);
        feedbackService.save(feedback);
        return ApiResponse.success(feedback);
    }
}
