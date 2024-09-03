package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.entity.Task;
import clara.oswald.enums.DispatchStrategyEnum;
import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class TaskDispatchService {

    private Set<Task> tasks = new ConcurrentHashSet<>();

    private Set<LiftInstance> instances;

    private Thread thread;

    private DispatchStrategyEnum dispatchStrategyEnum = DispatchStrategyEnum.OPT;

    private final Object lock = new Object();

    public TaskDispatchService(Set<LiftInstance> instances) {
        this.instances = instances;
    }

    /**
     * 注册实例
     *
     * @param instance
     */
    public void registerInstance(LiftInstance instance) {
        instances.add(instance);
    }

    public void removeInstance(LiftInstance instance) {
        instances.remove(instance);
    }

    public void stopInstance(LiftInstance instance) {
        tasks.addAll(instance.getTasks());
        instance.stop();
    }

    public void resumeInstance(LiftInstance instance) {
        instance.start();
    }

    public void addTask(Task task) {
        tasks.add(task);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void removeTask(Task task) {
        //todo
    }
    /**
     * 新增任务
     */
    /**
     * 启动
     */
    public void start() {
        thread = new Thread(this::dispatch, "task dispatch thread");
        thread.setDaemon(true);
        thread.start();
        log.info("调度开始...");
    }

    /**
     * 分发任务
     */
    public void dispatch() {
        while (true) {
            synchronized (lock) {
                if (tasks.isEmpty() || instances.isEmpty()) {
                    log.info("调度等待中....");
                    try {
                        lock.wait();
                        continue;
                    } catch (InterruptedException e) {
                    }
                }
            }
            //分发规则
            Optional<Task> opt = tasks.stream().findFirst();
            if (opt.isEmpty()) {
                continue;
            }
            log.info("task : {}", opt.get());
            Task task = opt.get();
            TaskDispatchStrategy taskDispatchStrategy = dispatchStrategyEnum.getTaskDispatchStrategy();
            LiftInstance liftInstance = taskDispatchStrategy.selectLiftInstance(task, instances);
            if (liftInstance != null) {
                liftInstance.registerTask(task);
                tasks.remove(task);
                log.info("task :{}, dispatch to liftInstance :{}", task, liftInstance.toString());
            }
        }
    }
}
