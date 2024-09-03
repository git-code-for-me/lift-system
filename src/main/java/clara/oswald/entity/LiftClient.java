package clara.oswald.entity;

import clara.oswald.service.LiftSystemServiceImpl;

public class LiftClient {
    private LiftSystemServiceImpl liftSystemService;

   public LiftClient(LiftSystemServiceImpl systemService){
       this.liftSystemService = systemService;
   }

   public void addTask(Task task){
       liftSystemService.addTask(task);
   }

}
