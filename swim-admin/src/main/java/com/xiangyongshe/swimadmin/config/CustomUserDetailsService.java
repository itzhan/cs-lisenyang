package com.xiangyongshe.swimadmin.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiangyongshe.swimadmin.entity.SysRole;
import com.xiangyongshe.swimadmin.entity.SysUser;
import com.xiangyongshe.swimadmin.entity.SysUserRole;
import com.xiangyongshe.swimadmin.mapper.SysRoleMapper;
import com.xiangyongshe.swimadmin.mapper.SysUserMapper;
import com.xiangyongshe.swimadmin.mapper.SysUserRoleMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleMapper sysRoleMapper;

    public CustomUserDetailsService(SysUserMapper sysUserMapper,
                                    SysUserRoleMapper sysUserRoleMapper,
                                    SysRoleMapper sysRoleMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", username)
                .eq("deleted", 0));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Long> roleIds = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>()
                .eq("user_id", user.getId()))
                .stream().map(SysUserRole::getRoleId).toList();
        List<SimpleGrantedAuthority> authorities = roleIds.isEmpty() ? List.of() :
                sysRoleMapper.selectBatchIds(roleIds)
                        .stream()
                        .filter(r -> r.getStatus() != null && r.getStatus() == 1)
                        .map(SysRole::getRoleKey)
                        .map(key -> key.startsWith("ROLE_") ? key : "ROLE_" + key)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), user.getStatus() != null && user.getStatus() == 1,
                true, true, true, authorities);
    }
}
