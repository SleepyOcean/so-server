package com.sleepy.crawler.task;

import com.sleepy.crawler.worker.movie.PiankuWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 服务启动后的任务
 *
 * @author gehoubao
 * @create 2020-11-17 21:35
 **/
@Component
public class StartupTask implements CommandLineRunner {

    @Autowired
    private PiankuWorker piankuWorker;

    @Override
    public void run(String... args) throws Exception {
        try {
            piankuWorker.produce();
        } finally {
            piankuWorker.printLastLog();
        }
    }
}