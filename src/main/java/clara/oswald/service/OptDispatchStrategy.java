package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.entity.Task;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class OptDispatchStrategy extends TaskDispatchStrategy{
    @Override
    public LiftInstance selectLiftInstance(Task task, Set<LiftInstance> instances) {
        Set<LiftInstance> collect = instances.stream().filter(i -> i.getDirection() == task.getDirection()).collect(Collectors.toSet());
        if (collect.isEmpty()) {
            return super.selectLiftInstance(task, instances);
        }
        LiftInstance result = collect.stream().findFirst().get();
        for (LiftInstance liftInstance : collect) {
            result=Math.abs(result.getN()-task.getN())<Math.abs(liftInstance.getN()-task.getN())?result:liftInstance;
        }
        return result;
    }
}
