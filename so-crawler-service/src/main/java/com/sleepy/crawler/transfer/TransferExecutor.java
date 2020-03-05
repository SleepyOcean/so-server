package com.sleepy.crawler.transfer;

import com.sleepy.crawler.dto.TransferDTO;

/**
 * @author ghb
 * @create 2020-03-05 17:32
 **/
public interface TransferExecutor {
    /**
     * 传输数据
     *
     * @param data
     */
    void transfer(TransferDTO data);
}
