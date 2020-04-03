package com.sleepy.security.service;

import com.sleepy.security.pojo.SecurityUserVO;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:06:55
 */
public interface SecurityUserService {
    /**
     * 新建用户
     *
     * @param vo
     * @return
     */
    String newUser(SecurityUserVO vo);
}
