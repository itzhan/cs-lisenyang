package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.CoachProfile;
import com.xiangyongshe.swimadmin.mapper.CoachProfileMapper;
import com.xiangyongshe.swimadmin.service.CoachProfileService;
import org.springframework.stereotype.Service;

@Service
public class CoachProfileServiceImpl extends ServiceImpl<CoachProfileMapper, CoachProfile> implements CoachProfileService {
}
