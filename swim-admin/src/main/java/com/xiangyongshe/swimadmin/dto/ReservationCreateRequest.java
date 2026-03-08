package com.xiangyongshe.swimadmin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationCreateRequest {
    @NotNull(message = "排课不能为空")
    private Long scheduleId;
}
