package clara.oswald;

import clara.oswald.service.LiftSystemService;
import clara.oswald.service.LiftSystemServiceImpl;

public class LiftSystemServer {
    private static final LiftSystemService systemService=new LiftSystemServiceImpl();

    public static void start(){
        systemService.start();
    }

    public static void main(String[] args) {
        start();
    }
}
