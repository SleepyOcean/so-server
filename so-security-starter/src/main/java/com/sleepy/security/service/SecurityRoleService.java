package com.sleepy.security.service;

import com.sleepy.security.pojo.SecurityRoleVO;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:16:54
 */
public interface SecurityRoleService {
    /**
     * 新增角色
     *
     * @param vo
     * @return
     */
    String newRole(SecurityRoleVO vo);

    /**
     * 获取角色
     *
     * @param vo
     * @return
     */
    String getRole(SecurityRoleVO vo);
}
