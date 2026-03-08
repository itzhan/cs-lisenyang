package com.xiangyongshe.swimadmin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangyongshe.swimadmin.common.ApiResponse;
import com.xiangyongshe.swimadmin.config.JwtUtil;
import com.xiangyongshe.swimadmin.dto.LoginRequest;
import com.xiangyongshe.swimadmin.dto.LoginResponse;
import com.xiangyongshe.swimadmin.dto.RegisterRequest;
import com.xiangyongshe.swimadmin.entity.SysRole;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.entity.SysUserRole;
import com.xiangyongshe.swimadmin.exception.BizException;
import com.xiangyongshe.swimadmin.mapper.SysRoleMapper;
import com.xiangyongshe.swimadmin.mapper.SysUserRoleMapper;
import com.xiangyongshe.swimadmin.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SysUserService sysUserService;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          SysUserService sysUserService,
                          SysUserRoleMapper sysUserRoleMapper,
                          SysRoleMapper sysRoleMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.sysUserService = sysUserService;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        if (!auth.isAuthenticated()) {
            throw new BizException("用户名或密码错误");
        }
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>()
                .eq("username", req.getUsername()));
        String roleKey = getPrimaryRole(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", roleKey);
        String token = jwtUtil.generateToken(claims);
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUsername(user.getUsername());
        resp.setRole(roleKey);
        return ApiResponse.success(resp);
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest req) {
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setRealName(req.getRealName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setStatus(1);
        sysUserService.createUserWithRole(user, "USER");
        return ApiResponse.success();
    }

    private String getPrimaryRole(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>()
                .eq("user_id", userId))
                .stream().map(SysUserRole::getRoleId).toList();
        if (roleIds.isEmpty()) {
            return "USER";
        }
        SysRole role = sysRoleMapper.selectById(roleIds.get(0));
        return role != null ? role.getRoleKey() : "USER";
    }
}
