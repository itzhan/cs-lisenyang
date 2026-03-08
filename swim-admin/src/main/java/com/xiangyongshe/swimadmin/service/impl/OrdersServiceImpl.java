package com.xiangyongshe.swimadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiangyongshe.swimadmin.entity.Orders;
import com.xiangyongshe.swimadmin.mapper.OrdersMapper;
import com.xiangyongshe.swimadmin.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
