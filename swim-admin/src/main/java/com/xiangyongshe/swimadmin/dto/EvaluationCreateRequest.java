package com.xiangyongshe.swimadmin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluationCreateRequest {
    @NotNull(message = "课程不能为空")
    private Long courseId;
    @NotNull(message = "预约不能为空")
    private Long reservationId;
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1")
    @Max(value = 5, message = "评分最高5")
    private Integer score;
    private String content;
}
