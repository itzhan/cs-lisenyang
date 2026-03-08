package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.CourseSchedule;
import com.xiangyongshe.swimadmin.mapper.CourseScheduleMapper;
import com.xiangyongshe.swimadmin.service.CourseScheduleService;
import org.springframework.stereotype.Service;

@Service
public class CourseScheduleServiceImpl extends ServiceImpl<CourseScheduleMapper, CourseSchedule> implements CourseScheduleService {
}
