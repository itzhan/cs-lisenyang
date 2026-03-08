package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.common.PageResult;
import com.xiangyongshe.swimadmin.entity.Evaluation;
import com.xiangyongshe.swimadmin.service.EvaluationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/evaluations")
public class AdminEvaluationController {

    private final EvaluationService evaluationService;

    public AdminEvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<PageResult<Evaluation>> list(@RequestParam(defaultValue = "1") long page,
                                                   @RequestParam(defaultValue = "10") long size) {
        QueryWrapper<Evaluation> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        Page<Evaluation> result = evaluationService.page(new Page<>(page, size), wrapper);
        return ApiResponse.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Evaluation evaluation = evaluationService.getById(id);
        if (evaluation == null) {
            return ApiResponse.error(404, "评价不存在");
        }
        evaluation.setDeleted(1);
        evaluationService.updateById(evaluation);
        return ApiResponse.success();
    }
}
