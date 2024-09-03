package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.entity.Task;
import cn.hutool.core.util.RandomUtil;

import java.util.Collection;
import java.util.Set;

public abstract class TaskDispatchStrategy {

    public LiftInstance selectLiftInstance(Task task, Set<LiftInstance> instances) {
        if (instances == null) {
            return null;
        }
        int size = instances.size();
        LiftInstance[] array = instances.toArray(a -> new LiftInstance[size]);
        return array[RandomUtil.randomInt(0, size)];
    }
}
