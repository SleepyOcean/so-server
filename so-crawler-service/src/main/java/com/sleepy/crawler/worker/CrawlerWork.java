package com.sleepy.crawler.worker;

import com.sleepy.crawler.dto.TransferDTO;

import java.io.IOException;
import java.util.List;

/**
 * @author ghb
 * @create 2020-03-05 17:42
 **/
public interface CrawlerWork {
    /**
     * 制造数据
     *
     * @return
     */
    List<TransferDTO> produce() throws IOException;
}
