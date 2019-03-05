package com.pinyougou.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * cron表达式：
 * second minute hour dayOfMonth month dayOfWeek year
 */
@Component
public class HelloTask {
    private static final Logger logger = LoggerFactory.getLogger(HelloTask.class);

    @Scheduled(cron = "1/3 * * * * ?")
    public void sayHello() {
        System.out.println("HelloTask sayHello() start run, " + new Date());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("HelloTask sayHello() end run, " + new Date());
    }
}
