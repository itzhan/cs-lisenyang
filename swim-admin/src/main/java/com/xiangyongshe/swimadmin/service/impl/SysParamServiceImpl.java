package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.SysParam;
import com.xiangyongshe.swimadmin.mapper.SysParamMapper;
import com.xiangyongshe.swimadmin.service.SysParamService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements SysParamService {
    @Override
    public String getValue(String key) {
        if (key == null || key.isBlank()) {
            return null;
        }
        SysParam param = getOne(new QueryWrapper<SysParam>()
                .eq("param_key", key)
                .eq("status", 1));
        return param == null ? null : param.getParamValue();
    }
}
