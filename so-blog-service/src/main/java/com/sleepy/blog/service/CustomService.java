package com.sleepy.blog.service;

import com.sleepy.blog.vo.custom.RequestVO;
import com.sleepy.common.exception.UserOperationIllegalException;
import com.sleepy.common.http.CommonDTO;

import java.util.Map;

/**
 * @author ghb
 * @create 2020-04-10 13:04
 **/

public interface CustomService {

    /**
     * get请求转发
     *
     * @param vo
     * @return
     */
    String requestGet(Map vo);

    /**
     * post请求转发
     *
     * @param vo
     * @return
     */
    String requestPost(Map vo);

    /**
     * 新建企业微信定时消息发送任务
     *
     * @param vo
     * @return
     * @throws UserOperationIllegalException
     */
    CommonDTO<String> requestTask(RequestVO vo) throws UserOperationIllegalException;

    /**
     * 取消企业微信定时消息发送任务
     *
     * @param vo
     * @return
     * @throws UserOperationIllegalException
     */
    CommonDTO<String> cancelRequestTask(Map vo) throws UserOperationIllegalException;

    /**
     * 获取企业微信定时消息发送任务列表
     *
     * @param user
     * @return
     */
    CommonDTO<String> getRequestTask(String user);

    /**
     * 恢复企业微信定时消息发送任务
     */
    void recoverScheduleTask();
}
