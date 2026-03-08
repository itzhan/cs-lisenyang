package com.xiangyongshe.swimadmin.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleCoachBindRequest {
    private List<Long> coachIds;
}
