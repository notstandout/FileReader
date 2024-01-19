package com.example.FileReader.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@EnableScheduling
public class ScheduledTask {

    @Scheduled(cron = "0 51 11 05 * ?")
    @Transactional
    public void create() {
    }
}
