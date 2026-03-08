package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.Reservation;
import com.xiangyongshe.swimadmin.mapper.ReservationMapper;
import com.xiangyongshe.swimadmin.service.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {
}
