package com.xiangyongshe.swimadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiangyongshe.swimadmin.entity.CourseScheduleCoach;

import java.util.List;

public interface CourseScheduleCoachService extends IService<CourseScheduleCoach> {
    void replaceScheduleCoaches(Long scheduleId, List<Long> coachIds);
}
