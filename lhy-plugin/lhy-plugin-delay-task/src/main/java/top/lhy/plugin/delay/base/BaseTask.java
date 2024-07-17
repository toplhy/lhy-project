package top.lhy.plugin.delay.base;

import cn.hutool.core.lang.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BaseTask {

    private String taskId = UUID.randomUUID().toString();

    public void run() {
        log.info("执行线程任务：{}", taskId);
    }
}
