package clara.oswald.service;

import clara.oswald.entity.LiftInstance;
import clara.oswald.enums.LiftStateEnum;
import clara.oswald.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LiftSystemServiceImpl implements LiftSystemService {

    private Set<LiftInstance> instances;

    private TaskDispatchService taskDispatchService;

    public LiftSystemServiceImpl(){
        instances=new CopyOnWriteArraySet<>();
        taskDispatchService = new TaskDispatchService(instances);
    }

    @Override
    public void start(){
        taskDispatchService.start();
    }

    /**
     * 注册实例
     */
    @Override
    public void registerLift(String liftNumber) {
        LiftInstance liftInstance = new LiftInstance(liftNumber);
        instances.add(liftInstance);
        liftInstance.start();
    }

    @Override
    public void removeLift(String liftNumber) {
        LiftInstance liftInstance = new LiftInstance(liftNumber);
        instances.remove(liftInstance);
        liftInstance.stop();
    }
    @Override
    public void resumeLift(String liftNumber){
        instances.forEach(instance->{
            if (instance.getNumber().equals(liftNumber)){
               taskDispatchService.resumeInstance(instance);
            }
        });
    }
    @Override
    public void stopLift(String liftNumber){
        instances.forEach(instance->{
            if (instance.getNumber().equals(liftNumber)){
                taskDispatchService.stopInstance(instance);
            }
        });
    }


    /**
     * 所有电梯信息
     */
    @Override
    public Set<LiftInstance> getLiftInstances(LiftStateEnum stateEnum){
        return instances.stream().filter(i -> {
            if (stateEnum == null) {
                return true;
            }
            if (i.getLiftState() == stateEnum) {
                return true;
            }
            return false;
        }).collect(Collectors.toSet());
    }

    @Override
    public void printInstances(LiftStateEnum stateEnum){
        getLiftInstances(stateEnum).forEach(i -> {
            System.out.println(i.toString());});
    }
    /**
     * 运行状态
     */
    @Override
    public Boolean isNormal(){
        for (LiftInstance instance : instances) {
            if (instance.isNormal()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addTask(Task task){
        if (instances.isEmpty()){
            log.error("请先添加电梯");
            return;
        }
        taskDispatchService.addTask(task);
    }

}
