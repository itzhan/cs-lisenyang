package com.xiangyongshe.swimadmin.dto;

import lombok.Data;

@Data
public class AdminUserUpdateRequest {
    private String realName;
    private String phone;
    private String email;
    private Integer status;
}
