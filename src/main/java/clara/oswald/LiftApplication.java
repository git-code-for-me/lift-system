package clara.oswald;

import clara.oswald.service.LiftSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiftApplication implements CommandLineRunner {

    @Autowired
    private LiftSystemService liftSystemService;

    public static void main(String[] args) {
        SpringApplication.run(LiftApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        liftSystemService.start();
        liftSystemService.registerLift("lift-3");
    }
}
