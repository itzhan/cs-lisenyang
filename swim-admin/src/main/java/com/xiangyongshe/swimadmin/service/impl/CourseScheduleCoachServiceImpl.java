package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.CourseScheduleCoach;
import com.xiangyongshe.swimadmin.mapper.CourseScheduleCoachMapper;
import com.xiangyongshe.swimadmin.service.CourseScheduleCoachService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseScheduleCoachServiceImpl extends ServiceImpl<CourseScheduleCoachMapper, CourseScheduleCoach>
        implements CourseScheduleCoachService {

    @Override
    @Transactional
    public void replaceScheduleCoaches(Long scheduleId, List<Long> coachIds) {
        if (scheduleId == null) {
            return;
        }
        UpdateWrapper<CourseScheduleCoach> update = new UpdateWrapper<>();
        update.eq("schedule_id", scheduleId).eq("deleted", 0).set("deleted", 1);
        this.update(update);
        if (coachIds == null || coachIds.isEmpty()) {
            return;
        }
        List<Long> distinctIds = coachIds.stream().distinct().toList();
        for (Long coachId : distinctIds) {
            CourseScheduleCoach rel = new CourseScheduleCoach();
            rel.setScheduleId(scheduleId);
            rel.setCoachId(coachId);
            rel.setDeleted(0);
            this.save(rel);
        }
    }
}
