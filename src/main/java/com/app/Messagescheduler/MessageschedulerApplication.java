package com.app.Messagescheduler;

import com.app.timer_tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Timer;


@SpringBootApplication
@ComponentScan({"com.*"})
@EnableScheduling
public class MessageschedulerApplication {

    @Autowired
    public Timer timer;

    @Autowired
    public Task task;


    public static void main(String[] args) throws InterruptedException, IOException {
        SpringApplication.run(MessageschedulerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startScheduler()
    {
        //run a task after every 1 minute....
        timer.schedule(task,1000,60000);

    }

}
