package top.lhy.plugin.delay.manger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.lhy.plugin.delay.base.BaseTask;
import top.lhy.plugin.delay.base.DelayTask;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

/**
 * 延时任务管理器
 */
@Component
@Slf4j
public class DelayQueueManager implements CommandLineRunner {

    private final DelayQueue<DelayTask> delayQueue = new DelayQueue<>();

    /**
     * 加入延时队列
     * @param task
     */
    public void put(DelayTask task) {
        log.info("加入延时任务：{}", task);
        delayQueue.put(task);
    }

    /**
     * 取消延时任务
     * @param task
     * @return
     */
    public boolean remove(DelayTask task) {
        log.info("取消延时任务：{}", task);
        return delayQueue.remove(task);
    }

    /**
     * 取消延时任务
     * @param taskId
     * @return
     */
    public boolean remove(String taskId) {
        log.info("取消延时任务：{}", taskId);
        return delayQueue.remove(new DelayTask(new BaseTask(), 0));
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化延时队列");
        Executors.newSingleThreadExecutor().execute(new Thread(this::excuteThread));
    }

    /**
     * 延时任务执行线程
     */
    private void excuteThread() {
        while (true) {
            try{
                DelayTask task = delayQueue.take();
                task.getData().run();
            }catch (InterruptedException e) {
                break;
            }
        }
    }

}
