package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.Feedback;
import com.xiangyongshe.swimadmin.mapper.FeedbackMapper;
import com.xiangyongshe.swimadmin.service.FeedbackService;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
}
