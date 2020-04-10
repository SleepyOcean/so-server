package com.sleepy.blog.service;

import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.vo.custom.RequestVO;
import com.sleepy.common.exception.UserOperationIllegalException;

import java.util.Map;

/**
 * @author ghb
 * @create 2020-04-10 13:04
 **/

public interface CustomService {

    String requestGet(Map vo);

    String requestPost(Map vo);

    CommonDTO<String> requestTask(RequestVO vo) throws UserOperationIllegalException;

    CommonDTO<String> cancelRequestTask(Map vo) throws UserOperationIllegalException;

    CommonDTO<String> getRequestTask(String user);

    void recoverScheduleTask();
}
