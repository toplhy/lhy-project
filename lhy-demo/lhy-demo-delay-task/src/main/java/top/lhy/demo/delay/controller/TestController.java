package top.lhy.demo.delay.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lhy.demo.delay.service.TestService;
import top.lhy.demo.delay.task.TestTask;
import top.lhy.plugin.delay.base.DelayTask;
import top.lhy.plugin.delay.manger.DelayQueueManager;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private DelayQueueManager delayQueueManager;
    @Resource
    private TestService testService;


    @RequestMapping("/")
    public String index() {
        delayQueueManager.put(new DelayTask(new TestTask() {
            @Override
            public void run() {
                super.run();
                testService.testRun();
            }
        }, 30000));

        delayQueueManager.put(new DelayTask(new TestTask() {
            @Override
            public void run() {
                super.run();
                testService.testRun();
            }
        }, 2000));
        return "ok";
    }
}
