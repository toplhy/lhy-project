package top.lhy.plugin.delay.base;

import cn.hutool.core.convert.Convert;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时任务
 */
@Getter
@Setter
public class DelayTask implements Delayed {

    // 任务参数
    final private BaseTask data;

    // 任务延时时间，单位毫秒
    final private long expire;

    public DelayTask(BaseTask data, long expire) {
        super();
        this.data = data;
        this.expire = expire + System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DelayTask) {
            return this.data.getTaskId().equals(((DelayTask) obj).getData().getTaskId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "DelayTask{" +
                "data=" + data +
                ", expire=" + expire +
                '}';
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        long delta = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return Convert.toInt(delta);
    }
}
