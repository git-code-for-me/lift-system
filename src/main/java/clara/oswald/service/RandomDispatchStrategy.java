package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.entity.Task;
import cn.hutool.core.util.RandomUtil;

import java.util.Collection;
import java.util.Set;

public class RandomDispatchStrategy extends TaskDispatchStrategy{
    @Override
    public LiftInstance selectLiftInstance(Task task, Set<LiftInstance> instances) {
       return super.selectLiftInstance(task, instances);
    }
}
