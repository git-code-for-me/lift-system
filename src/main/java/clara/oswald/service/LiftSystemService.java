package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.enums.LiftStateEnum;
import clara.oswald.entity.Task;

import java.util.Set;

public interface LiftSystemService {
    void start();

    void registerLift(String liftNumber);

    void removeLift(String liftNumber);

    void stopLift(String liftNumber);

    void resumeLift(String liftNumber);

    Set<LiftInstance> getLiftInstances(LiftStateEnum stateEnum);

    void printInstances(LiftStateEnum stateEnum);

    Boolean isNormal();

    void addTask(Task task);
}
