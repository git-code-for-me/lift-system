package clara.oswald.controller;

import clara.oswald.entity.LiftInstance;
import clara.oswald.entity.Task;
import clara.oswald.enums.DirectionEnum;
import clara.oswald.enums.LiftStateEnum;
import clara.oswald.service.LiftSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("lift-system")
public class LiftController {

    @Autowired
    private LiftSystemService liftSystemService;

    @PostMapping("lift/register/{liftNumber}")
    public Boolean registerLift(@PathVariable String liftNumber){
        liftSystemService.registerLift(liftNumber);
        return true;
    }
    @PostMapping("lift/remove/{liftNumber}")
    public Boolean moveLift(@PathVariable String liftNumber){
        liftSystemService.removeLift(liftNumber);
        return true;
    }
    @PostMapping("lift/resume/{liftNumber}")
    public Boolean resumeLift(@PathVariable String liftNumber){
        liftSystemService.resumeLift(liftNumber);
        return true;
    }
    @PostMapping("lift/stop/{liftNumber}")
    public Boolean stopLift(@PathVariable String liftNumber){
        liftSystemService.stopLift(liftNumber);
        return true;
    }
    @PostMapping({"lift/list/{state}","lift/list"})
    public Set<LiftInstance> getLiftInstances(@PathVariable(required = false) LiftStateEnum state){
        return liftSystemService.getLiftInstances(state);
    }
    @PostMapping("lift/add/{floorNumber}/{direction}")
    public Boolean addTask(@PathVariable Integer floorNumber, @PathVariable DirectionEnum direction){
        if (floorNumber==null || direction==null ){
            return false;
        }
        liftSystemService.addTask(new Task(floorNumber,direction));
        return true;
    }
}
