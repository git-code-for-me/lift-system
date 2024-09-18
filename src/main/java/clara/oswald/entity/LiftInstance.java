package clara.oswald.entity;

import clara.oswald.config.LiftConfig;
import clara.oswald.enums.DirectionEnum;
import clara.oswald.enums.LiftStateEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class LiftInstance {

    /**
     * 编号
     */
    @Getter
    private String number;

    private AtomicReference<LiftStateEnum> liftStateRef;

    private Map<DirectionEnum, PriorityQueue<Task>> taskMap = new HashMap<>();

    private Thread taskThread;

    private long speed = 1000;

    private LiftConfig config;

    private BlockingDeque<Object> alarmClock = new LinkedBlockingDeque<>();
    /**
     * 当前运行方向
     */
    @Getter
    private DirectionEnum direction;

    /**
     * 当前运行楼层
     */
    @Getter
    @Setter
    private int n;

    public LiftInstance(String number) {
        this.setN(1);
        this.number = number;
        liftStateRef = new AtomicReference<>(LiftStateEnum.IDLE);
        direction = DirectionEnum.IDLE;
        taskMap.put(DirectionEnum.UP, new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getN() - o2.getN();
            }
        }));
        taskMap.put(DirectionEnum.DOWN, new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o2.getN() - o1.getN();
            }
        }));
    }


    public void registerTask(final Task task) {
        log.info("registering task {}", task);
        if (!isNormal()) {
            return;
        }
        //添加任务规则
        if (task.getDirection() == DirectionEnum.UP) {
            taskMap.get(DirectionEnum.UP).offer(task);
        } else {
            taskMap.get(DirectionEnum.DOWN).offer(task);
        }
        alarmClock.add(new Object());
    }

    public void removeTask(final Task task) {
        //todo
    }

    public void start() {
        log.info("Starting lift number({})", number);
        if (taskThread == null || taskThread.getState() == Thread.State.TERMINATED || taskThread.getState() == Thread.State.NEW) {
            taskThread = new Thread(this::run, "lift number(" + number + ") task thread");
            taskThread.start();
            liftStateRef.set(LiftStateEnum.IDLE);
        }
    }

    public void stop() {
        log.info("Stopping lift number({})", number);
        if (!taskThread.isInterrupted()) {
            liftStateRef.set(LiftStateEnum.BREAK);
        }
    }

    private void run() {
        while (true) {

            if (!isNormal()) {
                return;
            }

            log.info("lift number:{},电梯状态: {},等待任务中...", number, liftStateRef.get());
            try {
                alarmClock.poll(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                continue;
            }
            int to = 0;
            //怎么执行任务

            PriorityQueue<Task> downQueue = taskMap.get(DirectionEnum.DOWN);
            Task downTask = downQueue.peek();
            PriorityQueue<Task> upQueue = taskMap.get(DirectionEnum.UP);
            Task upTask = upQueue.peek();
            if (downTask==null && upTask==null) {
                continue;
            }
            liftStateRef.set(LiftStateEnum.RUNNING);
            if (direction == DirectionEnum.UP) {
                if (upTask == null) {
                    direction = DirectionEnum.IDLE;
                    continue;
                } else {
                    Iterator<Task> iterator = upQueue.iterator();
                    while (iterator.hasNext()) {
                        Task t = iterator.next();
                        if (t.getN() >= getN()) {
                            to = t.getN();
                            iterator.remove();
                            break;
                        }
                    }
                    if (to == 0) {
                        direction = DirectionEnum.IDLE;
                    }
                }
            } else if (direction == DirectionEnum.DOWN) {
                if (downTask == null) {
                    direction = DirectionEnum.IDLE;
                    continue;
                } else {
                    Iterator<Task> iterator = downQueue.iterator();
                    while (iterator.hasNext()) {
                        Task t = iterator.next();
                        if (t.getN() <= getN()) {
                            to = t.getN();
                            iterator.remove();
                            break;
                        }
                    }
                    if (to == 0) {
                        direction = DirectionEnum.IDLE;
                    }
                }
            }

            if (to == 0) {
                direction = DirectionEnum.IDLE;
            }
            if (direction == DirectionEnum.IDLE) {
                if (downTask == null && upTask == null) {
                    continue;
                }
                if (downTask != null && upTask != null) {
                    if (Math.abs(downTask.getN() - getN()) < Math.abs(upTask.getN() - getN())) {
                        to = downTask.getN();
                        downQueue.poll();
                        direction = DirectionEnum.DOWN;
                    } else {
                        to = upTask.getN();
                        upQueue.poll();
                        direction = DirectionEnum.UP;
                    }
                } else if (upTask != null) {
                    to = upTask.getN();
                    upQueue.poll();
                    direction = DirectionEnum.UP;
                } else {
                    to = downTask.getN();
                    downQueue.poll();
                    direction = DirectionEnum.DOWN;
                }
            }
            this.running(getN(), to);
            this.setN(to);
            liftStateRef.set(LiftStateEnum.IDLE);
        }
    }

    private void running(int from, final int to) {
        while (from != to) {
            log.info("lift({})运行中..,当前楼层:{}", number, getN());
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
            }
            from += (from > to ? -1 : +1);
            this.setN(from);
        }
        log.info("lift({}),达到目的地:{}", number, getN());
    }

    /**
     * 当前实例是否正常
     *
     * @return
     */
    public Boolean isNormal() {
        return getLiftState().normal();
    }

    public LiftStateEnum getLiftState() {
        return liftStateRef.get();
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        taskMap.forEach((direction, p) -> {
            tasks.addAll(p);
        });
        return tasks;
    }

    @Override
    public String toString() {
        return "LiftInstance{" +
                "number='" + number + '\'' +
                "status='" + liftStateRef.get() + '\'' +
                '}';
    }

}
