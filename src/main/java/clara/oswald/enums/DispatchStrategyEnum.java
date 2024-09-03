package clara.oswald.enums;

import clara.oswald.service.OptDispatchStrategy;
import clara.oswald.service.RandomDispatchStrategy;
import clara.oswald.service.TaskDispatchStrategy;

public enum DispatchStrategyEnum {

    RANDOM(new RandomDispatchStrategy()),
    OPT(new OptDispatchStrategy());

    private TaskDispatchStrategy taskDispatchStrategy;

    DispatchStrategyEnum(TaskDispatchStrategy taskDispatchStrategy) {
        this.taskDispatchStrategy = taskDispatchStrategy;
    }

    public TaskDispatchStrategy getTaskDispatchStrategy() {
        return taskDispatchStrategy;
    }
}
