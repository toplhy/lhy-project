### 延时任务 delay-task
用于业务中延迟xx时间后执行某项动作。

#### 集成步骤
+ 添加依赖
```
<dependency>
    <groupId>top.lhy</groupId>
    <artifactId>lhy-plugin-delay-task</artifactId>
    <version>1.0.0</version>
</dependency>
```
+ 注入DelayQueueManager
```
@RestController
public class TestController {

    @Resource
    private DelayQueueManager delayQueueManager;

    @RequestMapping("/")
    public String index() {
        delayQueueManager.put(new DelayTask(new TestTask() {
            @Override
            public void run() {
                super.run();
            }
        }, 30000));

        delayQueueManager.put(new DelayTask(new TestTask() {
            @Override
            public void run() {
                super.run();
            }
        }, 2000));
        return "ok";
    }
}

```
